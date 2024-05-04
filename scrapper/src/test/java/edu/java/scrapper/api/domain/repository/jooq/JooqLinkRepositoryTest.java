package edu.java.scrapper.api.domain.repository.jooq;

import edu.java.scrapper.api.domain.repository.LinkRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JooqLinkRepositoryTest extends LinkRepositoryTest {

    @Autowired
    protected JooqLinkRepositoryTest(JooqLinkRepository repo) {
        super(repo);
    }
}
