package edu.java.scrapper.api.service.jooq;

import edu.java.dto.utils.LinkParser;
import edu.java.scrapper.api.domain.repository.jooq.JooqChatRepository;
import edu.java.scrapper.api.domain.repository.jooq.JooqLinkRepository;
import edu.java.scrapper.api.domain.repository.jooq.JooqSubscriptionRepository;
import edu.java.scrapper.api.service.abstr.AbstractLinkUpdater;
import edu.java.scrapper.api.service.updates.LinkUpdateSender;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.configuration.ClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Slf4j
public class JooqLinkUpdater extends AbstractLinkUpdater {

    public JooqLinkUpdater(
        ApplicationConfig config,
        ClientConfig clientConfig,
        LinkUpdateSender sender,
        LinkParser parser,
        JooqChatRepository chatRepo,
        JooqLinkRepository linkRepo,
        JooqSubscriptionRepository subscriptionRepo
    ) {
        super(config, clientConfig, sender, parser, chatRepo, linkRepo, subscriptionRepo);
    }
}
