package edu.java.scrapper.api.service.jpa;

import edu.java.dto.api.bot.ApiErrorResponse;
import edu.java.dto.api.bot.LinkUpdateRequest;
import edu.java.dto.api.exception.BotApiException;
import edu.java.dto.utils.LinkInfo;
import edu.java.dto.utils.LinkParser;
import edu.java.dto.utils.exception.NotUrlException;
import edu.java.dto.utils.exception.SourceException;
import edu.java.scrapper.api.domain.dto.jpa.Chat;
import edu.java.scrapper.api.domain.dto.jpa.Link;
import edu.java.scrapper.api.domain.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.api.service.LinkUpdater;
import edu.java.scrapper.client.sources.ResponseException;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.configuration.ClientConfig;
import edu.java.scrapper.shedule.update.dto.Update;
import edu.java.scrapper.shedule.update.sources.SourceUpdater;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Slf4j
public class JpaLinkUpdater implements LinkUpdater {
    private final ApplicationConfig.Scheduler scheduler;
    private final ClientConfig clientConfig;
    private final LinkParser parser;
    private final JpaLinkRepository linkRepo;

    public JpaLinkUpdater(
        ApplicationConfig config, ClientConfig clientConfig, LinkParser parser,
        JpaLinkRepository linkRepo
    ) {
        this.scheduler = config.scheduler();
        this.clientConfig = clientConfig;
        this.parser = parser;
        this.linkRepo = linkRepo;
    }

    @Override
    public int update() {
        OffsetDateTime lastCheck = OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
        List<Link> linksNeedToCheck = linkRepo.findByLastCheckLessThanEqual(
            lastCheck.minus(scheduler.forceCheckDelay()));
        int countUpdates = 0;
        for (Link link : linksNeedToCheck) {
            try {
                LinkInfo linkInfo = parser.parse(link.getUrl().toString());
                Optional<Update> update = getUpdateFromSource(linkInfo);
                if (update.isPresent() && update.get().getCreatedAt().isAfter(link.getLastUpdate())) {
                    countUpdates++;
                    link.setLastUpdate(update.get().getCreatedAt());
                    processLinkUpdate(update.get(), link);
                }
                link.setLastCheck(lastCheck);
                linkRepo.save(link);
            } catch (NotUrlException | SourceException | ResponseException e) {
                removeCorruptedLinkWithSubscriptions(link);
            }
        }
        return countUpdates;
    }

    private void processLinkUpdate(Update update, Link link) throws SourceException, NotUrlException {
        Set<Chat> subscribers = link.getChats();
        if (subscribers.isEmpty()) {
            linkRepo.delete(link);
            return;
        }
        try {
            clientConfig.botClient().sendUpdate(new LinkUpdateRequest(
                link.getId(),
                link.getUrl(),
                "Новое обновление по ссылке\n%s\n\nСоздано в %s по Гринвичу"
                    .formatted(link.getUrl(), update.getCreatedAt()
                        .format(DateTimeFormatter.ofPattern("HH:mm (dd.MM.yyyy г.)"))),
                subscribers.stream().map(Chat::getTgId).toArray(Long[]::new)
            ));
        } catch (BotApiException e) {
            ApiErrorResponse error = e.getError();
            log.error("%s: %s. %s: %s"
                .formatted(error.code(), error.description(), error.exceptionName(), error.exceptionMessage()));
        }
    }

    private Optional<Update> getUpdateFromSource(LinkInfo linkInfo) throws ResponseException {
        SourceUpdater updater = SourceUpdater.getUpdaterForSource(clientConfig, linkInfo.source());
        if (updater != null) {
            return updater.getUpdate(linkInfo.source());
        }
        return Optional.empty();
    }

    private void removeCorruptedLinkWithSubscriptions(Link link) {
        Set<Chat> subscribers = new HashSet<>(link.getChats());
        link.getChats().clear();
        linkRepo.delete(link);
        clientConfig.botClient().sendUpdate(new LinkUpdateRequest(
            link.getId(),
            link.getUrl(),
            "Ссылка\n" + link.getUrl() + "\n больше не доступна, поэтому она удалена из Ваших подписок",
            subscribers.stream().map(Chat::getTgId).toArray(Long[]::new)
        ));
    }
}
