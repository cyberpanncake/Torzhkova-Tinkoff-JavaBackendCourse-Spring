package edu.java.scrapper.api.service.jpa;

import edu.java.dto.api.scrapper.LinkResponse;
import edu.java.scrapper.api.domain.dto.jpa.Chat;
import edu.java.scrapper.api.domain.dto.jpa.Link;
import edu.java.scrapper.api.domain.repository.jpa.JpaChatRepository;
import edu.java.scrapper.api.domain.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.api.exception.chat.ChatNotFoundException;
import edu.java.scrapper.api.exception.link.LinkAdditionException;
import edu.java.scrapper.api.exception.link.LinkNotFoundException;
import edu.java.scrapper.api.service.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class JpaLinkService implements LinkService {
    private final JpaChatRepository chatRepo;
    private final JpaLinkRepository linkRepo;

    public JpaLinkService(JpaChatRepository chatRepo, JpaLinkRepository linkRepo) {
        this.chatRepo = chatRepo;
        this.linkRepo = linkRepo;
    }

    @Override
    public LinkResponse add(long tgId, URI url) throws ChatNotFoundException, LinkAdditionException {
        Chat chat = chatRepo.findByTgId(tgId).orElseThrow(ChatNotFoundException::new);
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
        Link link = linkRepo.findByUrl(url).orElse(new Link(url, now));
        if (link.getChats().contains(chat)) {
            throw new LinkAdditionException();
        }
        link.getChats().add(chat);
        chat.getLinks().add(link);
        chatRepo.save(chat);
        linkRepo.save(link);
        return new LinkResponse(link.getId(), link.getUrl());
    }

    @Override
    public LinkResponse remove(long tgId, URI url) throws LinkNotFoundException, ChatNotFoundException {
        Chat chat = chatRepo.findByTgId(tgId).orElseThrow(ChatNotFoundException::new);
        Link link = linkRepo.findByUrl(url).orElseThrow(LinkNotFoundException::new);
        if (!link.getChats().contains(chat)) {
            throw new LinkNotFoundException();
        }
        chat.getLinks().remove(link);
        link.getChats().remove(chat);
        chatRepo.save(chat);
        linkRepo.save(link);
        if (link.getChats().isEmpty()) {
            linkRepo.delete(link);
        }
        return new LinkResponse(link.getId(), link.getUrl());
    }

    @Override
    public List<LinkResponse> listAll(long tgId) throws ChatNotFoundException {
        Chat chat = chatRepo.findByTgId(tgId).orElseThrow(ChatNotFoundException::new);
        return chat.getLinks().stream()
            .map(l -> new LinkResponse(l.getId(), l.getUrl()))
            .toList();
    }
}
