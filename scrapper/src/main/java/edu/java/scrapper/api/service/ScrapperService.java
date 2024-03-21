package edu.java.scrapper.api.service;

import edu.java.scrapper.api.domain.repository.ChatRepository;
import edu.java.scrapper.api.domain.repository.LinkRepository;
import edu.java.scrapper.api.domain.repository.SubscriptionRepository;
import edu.java.scrapper.configuration.ApplicationConfig;

public abstract class ScrapperService {
    protected final ApplicationConfig config;
    protected final ChatRepository chatRepo;
    protected final LinkRepository linkRepo;
    protected final SubscriptionRepository subscriptionRepo;

    protected ScrapperService(
        ApplicationConfig config, ChatRepository chatRepo,
        LinkRepository linkRepo,
        SubscriptionRepository subscriptionRepo
    ) {
        this.config = config;
        this.chatRepo = chatRepo;
        this.linkRepo = linkRepo;
        this.subscriptionRepo = subscriptionRepo;
    }
}
