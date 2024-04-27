package edu.java.scrapper.api.service.jooq;

import edu.java.scrapper.api.domain.repository.jooq.JooqChatRepository;
import edu.java.scrapper.api.domain.repository.jooq.JooqLinkRepository;
import edu.java.scrapper.api.domain.repository.jooq.JooqSubscriptionRepository;
import edu.java.scrapper.api.service.abstr.AbstractChatService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class JooqChatService extends AbstractChatService {

    public JooqChatService(
        JooqChatRepository chatRepo, JooqLinkRepository linkRepo,
        JooqSubscriptionRepository subscriptionRepo
    ) {
        super(chatRepo, linkRepo, subscriptionRepo);
    }
}
