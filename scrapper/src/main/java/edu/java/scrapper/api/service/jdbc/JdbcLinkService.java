package edu.java.scrapper.api.service.jdbc;

import edu.java.scrapper.api.domain.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.api.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.api.domain.repository.jdbc.JdbcSubscriptionRepository;
import edu.java.scrapper.api.service.abstr.AbstractLinkService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class JdbcLinkService extends AbstractLinkService {

    public JdbcLinkService(
        JdbcChatRepository chatRepo, JdbcLinkRepository linkRepo,
        JdbcSubscriptionRepository subscriptionRepo
    ) {
        super(chatRepo, linkRepo, subscriptionRepo);
    }
}
