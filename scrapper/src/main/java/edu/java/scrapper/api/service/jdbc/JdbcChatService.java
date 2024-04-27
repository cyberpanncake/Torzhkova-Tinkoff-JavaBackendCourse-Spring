package edu.java.scrapper.api.service.jdbc;

import edu.java.scrapper.api.domain.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.api.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.api.domain.repository.jdbc.JdbcSubscriptionRepository;
import edu.java.scrapper.api.service.abstr.AbstractChatService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class JdbcChatService extends AbstractChatService {

    public JdbcChatService(
        JdbcChatRepository chatRepo, JdbcLinkRepository linkRepo,
        JdbcSubscriptionRepository subscriptionRepo
    ) {
        super(chatRepo, linkRepo, subscriptionRepo);
    }
}
