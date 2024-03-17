package edu.java.scrapper.api.service;

import edu.java.scrapper.api.domain.repository.ChatRepository;
import edu.java.scrapper.api.domain.repository.LinkRepository;
import edu.java.scrapper.api.domain.repository.SubscriptionRepository;

public abstract class ScrapperService {
    protected final ChatRepository chatRepo;
    protected final LinkRepository linkRepo;
    protected final SubscriptionRepository subscriptionRepo;

    protected ScrapperService(
        ChatRepository chatRepo,
        LinkRepository linkRepo,
        SubscriptionRepository subscriptionRepo
    ) {
        this.chatRepo = chatRepo;
        this.linkRepo = linkRepo;
        this.subscriptionRepo = subscriptionRepo;
    }
}
