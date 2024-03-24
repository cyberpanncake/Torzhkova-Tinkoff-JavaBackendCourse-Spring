package edu.java.scrapper.api.domain.repository;

import edu.java.scrapper.api.domain.dto.Chat;
import java.util.List;
import java.util.Optional;

public interface ChatRepository {
    Optional<Chat> findByTgId(long tgId);

    Chat add(long tgId);

    void remove(long tgId);

    List<Chat> findAll();
}
