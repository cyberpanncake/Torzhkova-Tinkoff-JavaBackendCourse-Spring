package edu.java.scrapper.api.service.jpa;

import edu.java.scrapper.api.domain.dto.jpa.Chat;
import edu.java.scrapper.api.domain.dto.jpa.Link;
import edu.java.scrapper.api.domain.repository.jpa.JpaChatRepository;
import edu.java.scrapper.api.domain.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.api.exception.chat.ChatAlreadyRegisteredException;
import edu.java.scrapper.api.exception.chat.ChatNotFoundException;
import edu.java.scrapper.api.service.ChatService;
import java.util.HashSet;
import java.util.Set;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class JpaChatService implements ChatService {
    private final JpaChatRepository chatRepo;
    private final JpaLinkRepository linkRepo;

    public JpaChatService(JpaChatRepository chatRepo, JpaLinkRepository linkRepo) {
        this.chatRepo = chatRepo;
        this.linkRepo = linkRepo;
    }

    @Override
    public void register(long tgId) throws ChatAlreadyRegisteredException {
        if (chatRepo.findByTgId(tgId).isPresent()) {
            throw new ChatAlreadyRegisteredException();
        }
        Chat chat = new Chat();
        chat.setTgId(tgId);
        chatRepo.save(chat);
    }

    @Override
    public void unregister(long tgId) throws ChatNotFoundException {
        Chat chat = chatRepo.findByTgId(tgId).orElseThrow(ChatNotFoundException::new);
        Set<Link> links = chat.getLinks();
        Set<Link> linksCopy = new HashSet<>(links);
        for (Link link : linksCopy) {
            links.remove(link);
            link.getChats().remove(chat);
            if (link.getChats().isEmpty()) {
                linkRepo.delete(link);
            }
        }
        chatRepo.delete(chat);
    }
}
