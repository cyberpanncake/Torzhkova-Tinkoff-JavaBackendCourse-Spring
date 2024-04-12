package edu.java.scrapper.api.domain.repository;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.api.domain.dto.Link;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
public abstract class LinkRepositoryTest extends IntegrationTest {
    private static final URI URL =
        URI.create("https://github.com/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse-Spring/");
    private static final OffsetDateTime NOW = OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
    private final LinkRepository repo;

    protected LinkRepositoryTest(LinkRepository repo) {
        this.repo = repo;
    }

    @Test
    @Rollback
    void addTest() {
        Link expected = new Link(null, URL, NOW, NOW);
        Link actual = repo.add(expected);
        Assertions.assertEquals(expected.url(), actual.url());
    }

    @Test
    @Rollback
    void addDuplicateExceptionTest() {
        Link link = new Link(null, URL, NOW, NOW);
        repo.add(link);
        Assertions.assertThrows(DuplicateKeyException.class, () -> repo.add(link));
    }

    @Test
    @Rollback
    void findByUrlTest() {
        Link link = new Link(null, URL, NOW, NOW);
        Link expected = repo.add(link);
        Link actual = repo.findByUrl(URL).get();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Rollback
    void findByUrlNotExistTest() {
        Optional<Link> actual = repo.findByUrl(URL);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    @Rollback
    void deleteTest() {
        Link link = new Link(null, URL, NOW, NOW);
        repo.add(link);
        repo.remove(URL);
        Optional<Link> actual = repo.findByUrl(URL);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    @Rollback
    void findAllTest() {
        Link link1 = new Link(null, URL, NOW, NOW);
        URI url2 = URI.create("https://github.com/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse/");
        Link link2 = new Link(null, url2, NOW, NOW);
        List<Link> expected = new ArrayList<>();
        expected.add(repo.add(link1));
        expected.add(repo.add(link2));
        List<Link> actual = repo.findAll();
        Assertions.assertEquals(expected, actual);
    }
}
