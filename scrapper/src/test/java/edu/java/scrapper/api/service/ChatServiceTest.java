package edu.java.scrapper.api.service;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.api.domain.repository.ChatRepository;
import edu.java.scrapper.api.exception.chat.ChatAlreadyRegisteredException;
import edu.java.scrapper.api.exception.chat.ChatNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class ChatServiceTest extends IntegrationTest {
    private static final long TG_ID = 11111;
    private final ChatService chatService;
    private final ChatRepository chatRepo;

    protected ChatServiceTest(ChatService chatService, ChatRepository chatRepo) {
        this.chatService = chatService;
        this.chatRepo = chatRepo;
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
