package edu.java.scrapper.api.service.jdbc;

import edu.java.dto.utils.LinkParser;
import edu.java.scrapper.api.domain.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.api.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.api.domain.repository.jdbc.JdbcSubscriptionRepository;
import edu.java.scrapper.api.service.abstr.AbstractLinkUpdater;
import edu.java.scrapper.api.service.updates.LinkUpdateSender;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.configuration.ClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Slf4j
public class JdbcLinkUpdater extends AbstractLinkUpdater {

    public JdbcLinkUpdater(
        ApplicationConfig config,
        ClientConfig clientConfig,
        LinkUpdateSender sender,
        LinkParser parser,
        JdbcChatRepository chatRepo,
        JdbcLinkRepository linkRepo,
        JdbcSubscriptionRepository subscriptionRepo
    ) {
        super(config, clientConfig, sender, parser, chatRepo, linkRepo, subscriptionRepo);
    }
}
