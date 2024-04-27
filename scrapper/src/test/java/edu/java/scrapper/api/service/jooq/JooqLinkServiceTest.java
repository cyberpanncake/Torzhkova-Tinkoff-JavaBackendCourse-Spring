package edu.java.scrapper.api.service.jooq;

import edu.java.scrapper.api.domain.repository.jooq.JooqLinkRepository;
import edu.java.scrapper.api.domain.repository.jooq.JooqSubscriptionRepository;
import edu.java.scrapper.api.service.ChatService;
import edu.java.scrapper.api.service.LinkService;
import edu.java.scrapper.api.service.LinkServiceTest;
import edu.java.scrapper.configuration.db.AccessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
public class JooqLinkServiceTest extends LinkServiceTest {

    @Autowired
    protected JooqLinkServiceTest(
        LinkService linkService, ChatService chatService,
        JooqLinkRepository linkRepo, JooqSubscriptionRepository subscriptionRepo
    ) {
        super(linkService, chatService, linkRepo, subscriptionRepo);
    }

    @DynamicPropertySource
    static void setAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> AccessType.JOOQ);
    }
}
