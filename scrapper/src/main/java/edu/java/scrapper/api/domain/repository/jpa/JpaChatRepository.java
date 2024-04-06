package edu.java.scrapper.api.domain.repository.jpa;

import edu.java.scrapper.api.domain.dto.jpa.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByTgId(long tgId);
}
