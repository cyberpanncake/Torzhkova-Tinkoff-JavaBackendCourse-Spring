package edu.java.scrapper.api.domain.repository.jpa;

import edu.java.scrapper.api.domain.dto.jpa.Chat;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByTgId(long tgId);
}
