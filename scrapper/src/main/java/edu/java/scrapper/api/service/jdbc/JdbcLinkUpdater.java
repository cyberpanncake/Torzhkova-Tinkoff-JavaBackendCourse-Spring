package edu.java.scrapper.api.service.jdbc;

import edu.java.dto.api.bot.LinkUpdateRequest;
import edu.java.dto.utils.LinkInfo;
import edu.java.dto.utils.exception.NotLinkException;
import edu.java.dto.utils.exception.SourceException;
import edu.java.scrapper.api.domain.dto.Chat;
import edu.java.scrapper.api.domain.dto.Link;
import edu.java.scrapper.api.domain.dto.Subscription;
import edu.java.scrapper.api.domain.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.api.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.api.domain.repository.jdbc.JdbcSubscriptionRepository;
import edu.java.scrapper.api.service.LinkUpdater;
import edu.java.scrapper.api.service.ScrapperService;
import edu.java.scrapper.client.sources.ResponseException;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.configuration.ClientConfig;
import edu.java.scrapper.configuration.LinkParserConfig;
import edu.java.scrapper.shedule.update.dto.Update;
import edu.java.scrapper.shedule.update.sources.SourceUpdater;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcLinkUpdater extends ScrapperService implements LinkUpdater {
    private final ClientConfig clientConfig;
    private final LinkParserConfig parserConfig;

    @Autowired
    protected JdbcLinkUpdater(
        ApplicationConfig config, LinkParserConfig parserConfig,
        ClientConfig clientConfig,
        JdbcChatRepository chatRepo, JdbcLinkRepository linkRepo,
        JdbcSubscriptionRepository subscriptionRepo
    ) {
        super(config, chatRepo, linkRepo, subscriptionRepo);
        this.parserConfig = parserConfig;
        this.clientConfig = clientConfig;
    }

    @Override
    public int update() {
        Duration checkDelay = config.scheduler().forceCheckDelay();
        OffsetDateTime lastCheck = OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
        List<Link> linksNeedToCheck = linkRepo.findAllWithLastCheckOlderThan(lastCheck.minus(checkDelay));
        int countUpdates = 0;
        for (Link link : linksNeedToCheck) {
            linkRepo.update(new Link(link.id(), link.url(), link.lastUpdate(), lastCheck));
            try {
                LinkInfo linkInfo = parserConfig.linkParser().parse(link.url().toString());
                Optional<Update> update = getUpdateFromSource(linkInfo);
                if (update.isPresent() && update.get().getCreatedAt().isAfter(link.lastUpdate())) {
                    countUpdates++;
                    linkRepo.update(new Link(link.id(), link.url(), update.get().getCreatedAt(), lastCheck));
                    processLinkUpdate(update.get(), link);
                }
            } catch (NotLinkException | SourceException | ResponseException e) {
                removeCorruptedLinkWithSubscriptions(link);
            }
        }
        return countUpdates;
    }

    private void processLinkUpdate(Update update, Link link) throws SourceException, NotLinkException {
        List<Chat> subscribers = subscriptionRepo.findAllChatsByLink(link);
        if (subscribers.isEmpty()) {
            linkRepo.remove(link.url());
            return;
        }
        clientConfig.botClient().sendUpdate(new LinkUpdateRequest(
            link.id(),
            link.url(),
            "Новое обновление по ссылке\n%s\nСоздано в %s"
                .formatted(link.url(), update.getCreatedAt()),
            subscribers.stream().map(Chat::tgId).toArray(Long[]::new)
        ));
    }

    private Optional<Update> getUpdateFromSource(LinkInfo linkInfo) throws ResponseException {
        SourceUpdater updater = SourceUpdater.getUpdaterForSource(clientConfig, linkInfo.source());
        if (updater != null) {
            return updater.getUpdate(linkInfo.source());
        }
        return Optional.empty();
    }

    private void removeCorruptedLinkWithSubscriptions(Link link) {
        List<Chat> subscribers = subscriptionRepo.findAllChatsByLink(link);
        for (Chat subscriber : subscribers) {
            Subscription subscription = new Subscription(subscriber.id(), link.id());
            subscriptionRepo.remove(subscription);
        }
        linkRepo.remove(link.url());
        clientConfig.botClient().sendUpdate(new LinkUpdateRequest(
            link.id(),
            link.url(),
            "Ссылка\n" + link.url() + "\n больше не доступна, поэтому она удалена из Ваших подписок",
            subscribers.stream().map(Chat::tgId).toArray(Long[]::new)
        ));
    }
}
