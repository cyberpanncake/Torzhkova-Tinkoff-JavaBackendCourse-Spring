package edu.java.scrapper.api.service.jpa;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.api.domain.repository.jpa.JpaChatRepository;
import edu.java.scrapper.api.exception.chat.ChatAlreadyRegisteredException;
import edu.java.scrapper.api.exception.chat.ChatNotFoundException;
import edu.java.scrapper.api.service.ChatService;
import edu.java.scrapper.configuration.db.AccessType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class JpaChatServiceTest extends IntegrationTest {
    private static final long TG_ID = 11111;
    private final ChatService chatService;
    private final JpaChatRepository chatRepo;

    @Autowired
    protected JpaChatServiceTest(ChatService chatService, JpaChatRepository chatRepo) {
        this.chatService = chatService;
        this.chatRepo = chatRepo;
    }

    @DynamicPropertySource
    static void setAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> AccessType.JPA);
    }

    @Test
    @Rollback
    void registerTest() throws ChatAlreadyRegisteredException {
        chatService.register(TG_ID);
        Assertions.assertTrue(chatRepo.findByTgId(TG_ID).isPresent());
    }

    @Test
    @Rollback
    void registerExceptionTest() throws ChatAlreadyRegisteredException {
        chatService.register(TG_ID);
        Assertions.assertThrows(ChatAlreadyRegisteredException.class, () -> chatService.register(TG_ID));
    }

    @Test
    @Rollback
    void unregisterTest() throws ChatAlreadyRegisteredException, ChatNotFoundException {
        chatService.register(TG_ID);
        chatService.unregister(TG_ID);
        Assertions.assertTrue(chatRepo.findByTgId(TG_ID).isEmpty());
    }

    @Test
    @Rollback
    void unregisterExceptionTest() {
        Assertions.assertThrows(ChatNotFoundException.class, () -> chatService.unregister(TG_ID));
    }
}
