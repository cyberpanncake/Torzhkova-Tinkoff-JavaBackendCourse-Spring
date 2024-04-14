package edu.java.scrapper.api.domain.repository.jdbc;

import edu.java.scrapper.api.domain.repository.LinkRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JdbcLinkRepositoryTest extends LinkRepositoryTest {

    @Autowired
    protected JdbcLinkRepositoryTest(JdbcLinkRepository repo) {
        super(repo);
    }
}
