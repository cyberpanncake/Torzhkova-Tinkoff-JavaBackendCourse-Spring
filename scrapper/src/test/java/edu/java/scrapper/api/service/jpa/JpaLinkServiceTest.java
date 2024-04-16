package edu.java.scrapper.api.service.jpa;

import edu.java.dto.api.scrapper.LinkResponse;
import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.api.domain.dto.jpa.Chat;
import edu.java.scrapper.api.domain.dto.jpa.Link;
import edu.java.scrapper.api.domain.repository.jpa.JpaChatRepository;
import edu.java.scrapper.api.domain.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.api.exception.chat.ChatAlreadyRegisteredException;
import edu.java.scrapper.api.exception.chat.ChatNotFoundException;
import edu.java.scrapper.api.exception.link.LinkAdditionException;
import edu.java.scrapper.api.exception.link.LinkNotFoundException;
import edu.java.scrapper.api.service.ChatService;
import edu.java.scrapper.api.service.LinkService;
import edu.java.scrapper.configuration.db.AccessType;
import java.net.URI;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
public class JpaLinkServiceTest extends IntegrationTest {
    private static final long TG_ID = 11111;
    private static final URI URL =
        URI.create("https://github.com/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse-Spring/");
    private final LinkService linkService;
    private final ChatService chatService;
    private final JpaChatRepository chatRepo;
    private final JpaLinkRepository linkRepo;

    @Autowired
    protected JpaLinkServiceTest(
        LinkService linkService, ChatService chatService,
        JpaChatRepository chatRepo, JpaLinkRepository linkRepo
    ) {
        this.linkService = linkService;
        this.chatService = chatService;
        this.chatRepo = chatRepo;
        this.linkRepo = linkRepo;
    }

    @DynamicPropertySource
    static void setAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> AccessType.JDBC);
    }

    @Test
    @Rollback
    void addTest() throws LinkAdditionException, ChatNotFoundException, ChatAlreadyRegisteredException {
        chatService.register(TG_ID);
        linkService.add(TG_ID, URL);
        Optional<Link> link = linkRepo.findByUrl(URL);
        Optional<Chat> chat = chatRepo.findByTgId(TG_ID);
        Assertions.assertTrue(link.isPresent() && chat.isPresent()
            && chat.get().getLinks().contains(link.get()));
    }

    @Test
    @Rollback
    void addChatNotFoundExceptionTest() {
        Assertions.assertThrows(ChatNotFoundException.class, () -> linkService.add(TG_ID, URL));
    }

    @Test
    @Rollback
    void addLinkAdditionExceptionTest()
        throws ChatAlreadyRegisteredException, LinkAdditionException, ChatNotFoundException {
        chatService.register(TG_ID);
        linkService.add(TG_ID, URL);
        Assertions.assertThrows(LinkAdditionException.class, () -> linkService.add(TG_ID, URL));
    }

    @Test
    @Rollback
    void removeTest()
        throws LinkAdditionException, ChatNotFoundException, ChatAlreadyRegisteredException, LinkNotFoundException {
        chatService.register(TG_ID);
        linkService.add(TG_ID, URL);
        linkService.remove(TG_ID, URL);
        Optional<Chat> chat = chatRepo.findByTgId(TG_ID);
        Assertions.assertTrue(linkRepo.findByUrl(URL).isEmpty() && chat.isPresent()
            && chat.get().getLinks().isEmpty());
    }

    @Test
    @Rollback
    void removeChatNotFoundExceptionTest() {
        Assertions.assertThrows(ChatNotFoundException.class, () -> linkService.remove(TG_ID, URL));
    }

    @Test
    @Rollback
    void removeLinkNotFoundExceptionTest() throws ChatAlreadyRegisteredException {
        chatService.register(TG_ID);
        Assertions.assertThrows(LinkNotFoundException.class, () -> linkService.remove(TG_ID, URL));
    }

    @Test
    @Rollback
    void listAllTest() throws LinkAdditionException, ChatNotFoundException, ChatAlreadyRegisteredException {
        URI url2 = URI.create("https://github.com/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse/");
        chatService.register(TG_ID);
        linkService.add(TG_ID, URL);
        linkService.add(TG_ID, url2);
        Set<URI> expected = Set.of(URL, url2);
        Set<URI> actual = linkService.listAll(TG_ID).stream()
            .map(LinkResponse::url)
            .collect(Collectors.toSet());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Rollback
    void listAllChatNotFoundExceptionTest() {
        Assertions.assertThrows(ChatNotFoundException.class, () -> linkService.listAll(TG_ID));
    }
}
