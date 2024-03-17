package edu.java.scrapper.api.service.jdbc;

import edu.java.scrapper.api.domain.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.api.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.api.domain.repository.jdbc.JdbcSubscriptionRepository;
import edu.java.scrapper.api.service.LinkUpdater;
import edu.java.scrapper.api.service.ScrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcLinkUpdater extends ScrapperService implements LinkUpdater {

    @Autowired
    protected JdbcLinkUpdater(
        JdbcChatRepository chatRepo, JdbcLinkRepository linkRepo,
        JdbcSubscriptionRepository subscriptionRepo
    ) {
        super(chatRepo, linkRepo, subscriptionRepo);
    }

    @Override
    public int update() {
        return 0;
    }
}
