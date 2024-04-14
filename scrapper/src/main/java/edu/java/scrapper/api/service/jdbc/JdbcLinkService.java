package edu.java.scrapper.api.service.jdbc;

import edu.java.scrapper.api.domain.dto.Chat;
import edu.java.scrapper.api.domain.dto.Link;
import edu.java.scrapper.api.domain.dto.Subscription;
import edu.java.scrapper.api.domain.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.api.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.api.domain.repository.jdbc.JdbcSubscriptionRepository;
import edu.java.scrapper.api.exception.chat.ChatNotFoundException;
import edu.java.scrapper.api.exception.link.LinkAdditionException;
import edu.java.scrapper.api.exception.link.LinkNotFoundException;
import edu.java.scrapper.api.service.LinkService;
import edu.java.scrapper.api.service.ScrapperService;
import edu.java.scrapper.configuration.ApplicationConfig;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class JdbcLinkService extends ScrapperService implements LinkService {

    @Autowired
    protected JdbcLinkService(
        ApplicationConfig config,
        JdbcChatRepository chatRepo, JdbcLinkRepository linkRepo,
        JdbcSubscriptionRepository subscriptionRepo
    ) {
        super(config, chatRepo, linkRepo, subscriptionRepo);
    }

    @Override
    public Link add(long tgId, URI url) throws ChatNotFoundException, LinkAdditionException {
        Optional<Subscription> subscription = subscriptionRepo.find(tgId, url);
        if (subscription.isPresent()) {
            throw new LinkAdditionException();
        }
        Optional<Chat> chat = chatRepo.findByTgId(tgId);
        if (chat.isEmpty()) {
            throw new ChatNotFoundException();
        }
        Optional<Link> link = linkRepo.findByUrl(url);
        if (link.isEmpty()) {
            OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
            link = Optional.of(linkRepo.add(new Link(null, url, now, now)));
        }
        subscriptionRepo.add(new Subscription(chat.get().id(), link.get().id()));
        return link.get();
    }

    @Override
    public Link remove(long tgId, URI url) throws LinkNotFoundException, ChatNotFoundException {
        Optional<Chat> chat = chatRepo.findByTgId(tgId);
        if (chat.isEmpty()) {
            throw new ChatNotFoundException();
        }
        Optional<Subscription> subscription = subscriptionRepo.find(tgId, url);
        if (subscription.isEmpty()) {
            throw new LinkNotFoundException();
        }
        Link link = linkRepo.findById(subscription.get().linkId()).get();
        subscriptionRepo.remove(subscription.get());
        if (subscriptionRepo.linkNotFollowedByAnyone(link.id())) {
            linkRepo.remove(link.id());
        }
        return link;
    }

    @Override
    public List<Link> listAll(long tgId) throws ChatNotFoundException {
        Optional<Chat> chat = chatRepo.findByTgId(tgId);
        if (chat.isEmpty()) {
            throw new ChatNotFoundException();
        }
        return subscriptionRepo.findAllLinksByChat(chat.get());
    }
}
