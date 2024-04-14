package edu.java.scrapper.api.domain.repository;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.api.domain.dto.Chat;
import edu.java.scrapper.api.domain.dto.Link;
import edu.java.scrapper.api.domain.dto.Subscription;
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

public abstract class SubscriptionRepositoryTest extends IntegrationTest {
    private static final long TG_ID = 11111;
    private static final URI URL =
        URI.create("https://github.com/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse-Spring/");
    private static final OffsetDateTime NOW = OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
    private final ChatRepository chatRepo;
    private final LinkRepository linkRepo;
    private final SubscriptionRepository repo;

    protected SubscriptionRepositoryTest(ChatRepository chatRepo,
        LinkRepository linkRepo, SubscriptionRepository repo) {
        this.chatRepo = chatRepo;
        this.linkRepo = linkRepo;
        this.repo = repo;
    }

    @Test
    @Transactional
    @Rollback
    void addTest() {
        Chat chat = chatRepo.add(TG_ID);
        Link link = linkRepo.add(new Link(null, URL, NOW, NOW));
        Subscription expected = new Subscription(chat.id(), link.id());
        Subscription actual = repo.add(expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    void addDuplicateExceptionTest() {
        Chat chat = chatRepo.add(TG_ID);
        Link link = linkRepo.add(new Link(null, URL, NOW, NOW));
        Subscription subscription = new Subscription(chat.id(), link.id());
        repo.add(subscription);
        Assertions.assertThrows(DuplicateKeyException.class, () -> repo.add(subscription));
    }

    @Test
    @Transactional
    @Rollback
    void findTest() {
        Chat chat = chatRepo.add(TG_ID);
        Link link = linkRepo.add(new Link(null, URL, NOW, NOW));
        Subscription subscription = new Subscription(chat.id(), link.id());
        Subscription expected = repo.add(subscription);
        Subscription actual = repo.find(TG_ID, URL).get();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    void findNotExistTest() {
        Chat chat = chatRepo.add(TG_ID);
        Link link = linkRepo.add(new Link(null, URL, NOW, NOW));
        Subscription subscription = new Subscription(chat.id(), link.id());
        Optional<Subscription> actual = repo.find(TG_ID, URL);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void deleteTest() {
        Chat chat = chatRepo.add(TG_ID);
        Link link = linkRepo.add(new Link(null, URL, NOW, NOW));
        Subscription subscription = new Subscription(chat.id(), link.id());
        repo.add(subscription);
        repo.remove(subscription);
        Optional<Subscription> actual = repo.find(TG_ID, URL);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        Chat chat1 = chatRepo.add(TG_ID);
        Chat chat2 = chatRepo.add(22222);
        Link link = linkRepo.add(new Link(null, URL, NOW, NOW));
        Subscription subscription1 = new Subscription(chat1.id(), link.id());
        Subscription subscription2 = new Subscription(chat2.id(), link.id());
        List<Subscription> expected = new ArrayList<>();
        expected.add(repo.add(subscription1));
        expected.add(repo.add(subscription2));
        List<Subscription> actual = repo.findAll();
        Assertions.assertEquals(expected, actual);
    }
}
