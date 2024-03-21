package edu.java.scrapper.api.service.jdbc;

import edu.java.scrapper.api.domain.dto.Link;
import edu.java.scrapper.api.domain.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.api.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.api.domain.repository.jdbc.JdbcSubscriptionRepository;
import edu.java.scrapper.api.service.LinkUpdater;
import edu.java.scrapper.api.service.ScrapperService;
import edu.java.scrapper.configuration.ApplicationConfig;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcLinkUpdater extends ScrapperService implements LinkUpdater {

    @Autowired
    protected JdbcLinkUpdater(
        ApplicationConfig config,
        JdbcChatRepository chatRepo, JdbcLinkRepository linkRepo,
        JdbcSubscriptionRepository subscriptionRepo
    ) {
        super(config, chatRepo, linkRepo, subscriptionRepo);
    }

    @Override
    public int update() {
        Duration checkDelay = config.scheduler().forceCheckDelay();
        OffsetDateTime lastCheck = OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS)
            .minus(checkDelay);
        List<Link> linksNeedToCheck = linkRepo.findAllWithLastCheckOlderThan(lastCheck);
        int countUpdates = 0;
        for (Link link : linksNeedToCheck) {

        }
        return countUpdates;
    }
}
