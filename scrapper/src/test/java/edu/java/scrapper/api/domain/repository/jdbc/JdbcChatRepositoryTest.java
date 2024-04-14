package edu.java.scrapper.api.domain.repository.jdbc;

import edu.java.scrapper.api.domain.repository.ChatRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JdbcChatRepositoryTest extends ChatRepositoryTest {

    @Autowired
    protected JdbcChatRepositoryTest(JdbcChatRepository repo) {
        super(repo);
    }
}
