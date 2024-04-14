package edu.java.scrapper.api.domain.repository;

import edu.java.scrapper.api.domain.dto.Chat;
import edu.java.scrapper.api.domain.dto.Link;
import edu.java.scrapper.api.domain.dto.Subscription;
import java.net.URI;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository {
    Optional<Subscription> find(long tgId, URI url);

    Subscription add(Subscription subscription);

    void remove(Subscription subscription);

    List<Subscription> findAll();

    List<Subscription> findAllByChat(Chat chat);

    List<Subscription> findAllByLink(Link link);

    boolean linkNotFollowedByAnyone(long linkId);

    List<Link> findAllLinksByChat(Chat chat);

    List<Chat> findAllChatsByLink(Link link);
}
