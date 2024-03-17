package edu.java.scrapper.api.domain.repository.jdbc;

import edu.java.scrapper.api.domain.repository.SubscriptionRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JdbcSubscriptionRepositoryTest extends SubscriptionRepositoryTest {

    @Autowired
    protected JdbcSubscriptionRepositoryTest(JdbcSubscriptionRepository repo, JdbcChatRepository chatRepo,
        JdbcLinkRepository linkRepo) {
        super(chatRepo, linkRepo, repo);
    }
}
