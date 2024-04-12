package edu.java.scrapper.api.service;

import edu.java.dto.api.scrapper.LinkResponse;
import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.api.domain.repository.LinkRepository;
import edu.java.scrapper.api.domain.repository.SubscriptionRepository;
import edu.java.scrapper.api.exception.chat.ChatAlreadyRegisteredException;
import edu.java.scrapper.api.exception.chat.ChatNotFoundException;
import edu.java.scrapper.api.exception.link.LinkAdditionException;
import edu.java.scrapper.api.exception.link.LinkNotFoundException;
import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class LinkServiceTest extends IntegrationTest {
    private static final long TG_ID = 11111;
    private static final URI URL =
        URI.create("https://github.com/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse-Spring/");
    private final LinkService linkService;
    private final ChatService chatService;
    private final LinkRepository linkRepo;
    private final SubscriptionRepository subscriptionRepo;

    protected LinkServiceTest(
        LinkService linkService, ChatService chatService,
        LinkRepository linkRepo, SubscriptionRepository subscriptionRepo
    ) {
        this.linkService = linkService;
        this.chatService = chatService;
        this.linkRepo = linkRepo;
        this.subscriptionRepo = subscriptionRepo;
    }

    @Test
    @Rollback
    void addTest() throws LinkAdditionException, ChatNotFoundException, ChatAlreadyRegisteredException {
        chatService.register(TG_ID);
        linkService.add(TG_ID, URL);
        Assertions.assertTrue(linkRepo.findByUrl(URL).isPresent()
            && subscriptionRepo.find(TG_ID, URL).isPresent());
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
        Assertions.assertTrue(linkRepo.findByUrl(URL).isEmpty()
            && subscriptionRepo.find(TG_ID, URL).isEmpty());
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
