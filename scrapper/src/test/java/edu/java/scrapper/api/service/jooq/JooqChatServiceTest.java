package edu.java.scrapper.api.service.jooq;

import edu.java.scrapper.api.domain.repository.jooq.JooqChatRepository;
import edu.java.scrapper.api.service.ChatService;
import edu.java.scrapper.api.service.ChatServiceTest;
import edu.java.scrapper.configuration.db.AccessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
public class JooqChatServiceTest extends ChatServiceTest {

    @Autowired
    protected JooqChatServiceTest(ChatService chatService, JooqChatRepository chatRepo) {
        super(chatService, chatRepo);
    }

    @DynamicPropertySource
    static void setAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> AccessType.JOOQ);
    }
}
