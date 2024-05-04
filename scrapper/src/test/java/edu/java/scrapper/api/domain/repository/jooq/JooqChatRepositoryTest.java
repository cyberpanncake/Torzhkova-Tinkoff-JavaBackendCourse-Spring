package edu.java.scrapper.api.domain.repository.jooq;

import edu.java.scrapper.api.domain.repository.ChatRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JooqChatRepositoryTest extends ChatRepositoryTest {

    @Autowired
    protected JooqChatRepositoryTest(JooqChatRepository repo) {
        super(repo);
    }
}
