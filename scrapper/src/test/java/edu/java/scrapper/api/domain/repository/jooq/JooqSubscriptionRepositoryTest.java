package edu.java.scrapper.api.domain.repository.jooq;

import edu.java.scrapper.api.domain.repository.SubscriptionRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JooqSubscriptionRepositoryTest extends SubscriptionRepositoryTest {

    @Autowired
    protected JooqSubscriptionRepositoryTest(
        JooqSubscriptionRepository repo, JooqChatRepository chatRepo,
        JooqLinkRepository linkRepo
    ) {
        super(chatRepo, linkRepo, repo);
    }
}
