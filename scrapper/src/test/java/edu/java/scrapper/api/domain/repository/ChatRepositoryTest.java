package edu.java.scrapper.api.domain.repository;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.api.domain.dto.Chat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class ChatRepositoryTest extends IntegrationTest {
    private static final long TG_ID = 11111;
    private final ChatRepository repo;

    protected ChatRepositoryTest(ChatRepository repo) {
        this.repo = repo;
    }

    @Test
    @Transactional
    @Rollback
    void addTest() {
        Chat actual = repo.add(TG_ID);
        Assertions.assertEquals(TG_ID, actual.tgId());
    }

    @Test
    @Transactional
    @Rollback
    void addDuplicateExceptionTest() {
        repo.add(TG_ID);
        Assertions.assertThrows(DuplicateKeyException.class, () -> repo.add(TG_ID));
    }

    @Test
    @Transactional
    @Rollback
    void findByTgIdTest() {
        Chat expected = repo.add(TG_ID);
        Chat actual = repo.findByTgId(TG_ID).get();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    void findByTgIdNotExistTest() {
        Optional<Chat> actual = repo.findByTgId(TG_ID);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void deleteTest() {
        repo.add(TG_ID);
        repo.remove(TG_ID);
        Optional<Chat> actual = repo.findByTgId(TG_ID);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        List<Chat> expected = new ArrayList<>();
        expected.add(repo.add(TG_ID));
        expected.add(repo.add(22222));
        List<Chat> actual = repo.findAll();
        Assertions.assertEquals(expected, actual);
    }
}
