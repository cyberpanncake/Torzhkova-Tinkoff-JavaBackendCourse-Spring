package edu.java.scrapper.api.service.jooq;

import edu.java.scrapper.api.domain.repository.jooq.JooqChatRepository;
import edu.java.scrapper.api.domain.repository.jooq.JooqLinkRepository;
import edu.java.scrapper.api.domain.repository.jooq.JooqSubscriptionRepository;
import edu.java.scrapper.api.service.abstr.AbstractLinkService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class JooqLinkService extends AbstractLinkService {

    public JooqLinkService(
        JooqChatRepository chatRepo, JooqLinkRepository linkRepo,
        JooqSubscriptionRepository subscriptionRepo
    ) {
        super(chatRepo, linkRepo, subscriptionRepo);
    }
}
