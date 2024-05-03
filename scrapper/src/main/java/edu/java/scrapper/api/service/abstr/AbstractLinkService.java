package edu.java.scrapper.api.service.abstr;

import edu.java.dto.api.scrapper.LinkResponse;
import edu.java.scrapper.api.domain.dto.Chat;
import edu.java.scrapper.api.domain.dto.Link;
import edu.java.scrapper.api.domain.dto.Subscription;
import edu.java.scrapper.api.domain.repository.ChatRepository;
import edu.java.scrapper.api.domain.repository.LinkRepository;
import edu.java.scrapper.api.domain.repository.SubscriptionRepository;
import edu.java.scrapper.api.exception.chat.ChatNotFoundException;
import edu.java.scrapper.api.exception.link.LinkAdditionException;
import edu.java.scrapper.api.exception.link.LinkNotFoundException;
import edu.java.scrapper.api.service.LinkService;
import edu.java.scrapper.api.service.ScrapperService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractLinkService extends ScrapperService implements LinkService {

    public AbstractLinkService(
        ChatRepository chatRepo, LinkRepository linkRepo,
        SubscriptionRepository subscriptionRepo
    ) {
        super(chatRepo, linkRepo, subscriptionRepo);
    }

    @Override
    public LinkResponse add(long tgId, URI url) throws ChatNotFoundException, LinkAdditionException {
        subscriptionRepo.find(tgId, url).ifPresent(s -> {
            throw new LinkAdditionException();
        });
        Chat chat = chatRepo.findByTgId(tgId).orElseThrow(ChatNotFoundException::new);
        Link link = linkRepo.findByUrl(url).orElseGet(() -> {
            OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
            return linkRepo.add(new Link(null, url, now, now));
        });
        subscriptionRepo.add(new Subscription(chat.id(), link.id()));
        return new LinkResponse(link.id(), link.url());
    }

    @Override
    public LinkResponse remove(long tgId, URI url) throws LinkNotFoundException, ChatNotFoundException {
        chatRepo.findByTgId(tgId).orElseThrow(ChatNotFoundException::new);
        Subscription subscription = subscriptionRepo.find(tgId, url).orElseThrow(LinkNotFoundException::new);
        Link link = linkRepo.findById(subscription.linkId()).get();
        subscriptionRepo.remove(subscription);
        if (subscriptionRepo.linkNotFollowedByAnyone(link.id())) {
            linkRepo.remove(link.id());
        }
        return new LinkResponse(link.id(), link.url());
    }

    @Override
    public List<LinkResponse> listAll(long tgId) throws ChatNotFoundException {
        Chat chat = chatRepo.findByTgId(tgId).orElseThrow(ChatNotFoundException::new);
        return subscriptionRepo.findAllLinksByChat(chat).stream()
            .map(l -> new LinkResponse(l.id(), l.url()))
            .toList();
    }
}
