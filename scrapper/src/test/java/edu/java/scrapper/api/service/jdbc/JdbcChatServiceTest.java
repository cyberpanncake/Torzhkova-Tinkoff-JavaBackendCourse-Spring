package edu.java.scrapper.api.service.jdbc;

import edu.java.scrapper.api.domain.repository.ChatRepository;
import edu.java.scrapper.api.service.ChatService;
import edu.java.scrapper.api.service.ChatServiceTest;
import edu.java.scrapper.configuration.db.AccessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
public class JdbcChatServiceTest extends ChatServiceTest {

    @Autowired
    protected JdbcChatServiceTest(ChatService chatService, ChatRepository chatRepo) {
        super(chatService, chatRepo);
    }

    @DynamicPropertySource
    static void setAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> AccessType.JDBC);
    }
}
