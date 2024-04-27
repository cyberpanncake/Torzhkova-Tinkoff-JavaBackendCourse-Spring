package edu.java.scrapper.configuration.db;

import edu.java.dto.utils.LinkParser;
import edu.java.scrapper.api.domain.repository.jooq.JooqChatRepository;
import edu.java.scrapper.api.domain.repository.jooq.JooqLinkRepository;
import edu.java.scrapper.api.domain.repository.jooq.JooqSubscriptionRepository;
import edu.java.scrapper.api.service.ChatService;
import edu.java.scrapper.api.service.LinkService;
import edu.java.scrapper.api.service.LinkUpdater;
import edu.java.scrapper.api.service.jooq.JooqChatService;
import edu.java.scrapper.api.service.jooq.JooqLinkService;
import edu.java.scrapper.api.service.jooq.JooqLinkUpdater;
import edu.java.scrapper.api.service.updates.LinkUpdateSender;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.configuration.ClientConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfig {

    @Bean
    public ChatService chatService(JooqChatRepository chatRepo, JooqLinkRepository linkRepo,
        JooqSubscriptionRepository subscriptionRepo) {
        return new JooqChatService(chatRepo, linkRepo, subscriptionRepo);
    }

    @Bean
    public LinkService linkService(JooqChatRepository chatRepo, JooqLinkRepository linkRepo,
        JooqSubscriptionRepository subscriptionRepo) {
        return new JooqLinkService(chatRepo, linkRepo, subscriptionRepo);
    }

    @Bean
    public LinkUpdater linkUpdater(ApplicationConfig config, ClientConfig clientConfig, LinkUpdateSender sender,
        LinkParser parser, JooqChatRepository chatRepo, JooqLinkRepository linkRepo,
        JooqSubscriptionRepository subscriptionRepo) {
        return new JooqLinkUpdater(config, clientConfig, sender, parser,
            chatRepo, linkRepo, subscriptionRepo);
    }
}
