package edu.java.scrapper.configuration.db;

import edu.java.dto.utils.LinkParser;
import edu.java.scrapper.api.domain.repository.jpa.JpaChatRepository;
import edu.java.scrapper.api.domain.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.api.service.ChatService;
import edu.java.scrapper.api.service.LinkService;
import edu.java.scrapper.api.service.LinkUpdater;
import edu.java.scrapper.api.service.jpa.JpaChatService;
import edu.java.scrapper.api.service.jpa.JpaLinkService;
import edu.java.scrapper.api.service.jpa.JpaLinkUpdater;
import edu.java.scrapper.api.service.updates.LinkUpdateSender;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.configuration.ClientConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfig {

    @Bean
    public ChatService chatService(JpaChatRepository chatRepo, JpaLinkRepository linkRepo) {
        return new JpaChatService(chatRepo, linkRepo);
    }

    @Bean
    public LinkService linkService(JpaChatRepository chatRepo, JpaLinkRepository linkRepo) {
        return new JpaLinkService(chatRepo, linkRepo);
    }

    @Bean
    public LinkUpdater linkUpdater(
        ApplicationConfig config, ClientConfig clientConfig, LinkUpdateSender sender, LinkParser parser,
        JpaLinkRepository linkRepo
    ) {
        return new JpaLinkUpdater(config, clientConfig, sender, parser, linkRepo);
    }
}
