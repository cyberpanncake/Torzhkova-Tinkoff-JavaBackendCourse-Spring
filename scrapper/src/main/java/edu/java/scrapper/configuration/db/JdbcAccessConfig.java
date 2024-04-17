package edu.java.scrapper.configuration.db;

import edu.java.dto.utils.LinkParser;
import edu.java.scrapper.api.domain.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.api.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.api.domain.repository.jdbc.JdbcSubscriptionRepository;
import edu.java.scrapper.api.service.ChatService;
import edu.java.scrapper.api.service.LinkService;
import edu.java.scrapper.api.service.LinkUpdater;
import edu.java.scrapper.api.service.jdbc.JdbcChatService;
import edu.java.scrapper.api.service.jdbc.JdbcLinkService;
import edu.java.scrapper.api.service.jdbc.JdbcLinkUpdater;
import edu.java.scrapper.api.service.updates.LinkUpdateSender;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.configuration.ClientConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfig {

    @Bean
    public ChatService chatService(JdbcChatRepository chatRepo, JdbcLinkRepository linkRepo,
        JdbcSubscriptionRepository subscriptionRepo) {
        return new JdbcChatService(chatRepo, linkRepo, subscriptionRepo);
    }

    @Bean
    public LinkService linkService(JdbcChatRepository chatRepo, JdbcLinkRepository linkRepo,
        JdbcSubscriptionRepository subscriptionRepo) {
        return new JdbcLinkService(chatRepo, linkRepo, subscriptionRepo);
    }

    @Bean
    public LinkUpdater linkUpdater(ApplicationConfig config, ClientConfig clientConfig, LinkUpdateSender sender,
        LinkParser parser, JdbcChatRepository chatRepo, JdbcLinkRepository linkRepo,
        JdbcSubscriptionRepository subscriptionRepo) {
        return new JdbcLinkUpdater(config, clientConfig, sender, parser,
            chatRepo, linkRepo, subscriptionRepo);
    }
}
