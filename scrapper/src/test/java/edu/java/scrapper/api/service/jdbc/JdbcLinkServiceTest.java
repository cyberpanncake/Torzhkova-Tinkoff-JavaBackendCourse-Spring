package edu.java.scrapper.api.service.jdbc;

import edu.java.scrapper.api.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.api.domain.repository.jdbc.JdbcSubscriptionRepository;
import edu.java.scrapper.api.service.ChatService;
import edu.java.scrapper.api.service.LinkService;
import edu.java.scrapper.api.service.LinkServiceTest;
import edu.java.scrapper.configuration.db.AccessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
public class JdbcLinkServiceTest extends LinkServiceTest {

    @Autowired
    protected JdbcLinkServiceTest(
        LinkService linkService, ChatService chatService,
        JdbcLinkRepository linkRepo, JdbcSubscriptionRepository subscriptionRepo
    ) {
        super(linkService, chatService, linkRepo, subscriptionRepo);
    }

    @DynamicPropertySource
    static void setAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> AccessType.JDBC);
    }
}
