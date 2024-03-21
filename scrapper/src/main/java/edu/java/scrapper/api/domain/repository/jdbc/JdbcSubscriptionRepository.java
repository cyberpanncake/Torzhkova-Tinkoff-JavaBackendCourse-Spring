package edu.java.scrapper.api.domain.repository.jdbc;

import edu.java.scrapper.api.domain.dto.Chat;
import edu.java.scrapper.api.domain.dto.Link;
import edu.java.scrapper.api.domain.dto.Subscription;
import edu.java.scrapper.api.domain.repository.SubscriptionRepository;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class JdbcSubscriptionRepository extends AbstractJdbcRepository implements SubscriptionRepository {

    public JdbcSubscriptionRepository(JdbcClient client) {
        super(client);
    }

    @Override
    public Optional<Subscription> find(long tgId, URI url) {
        return client.sql("""
                select subscription.*
                from subscription
                    inner join chat on subscription.chat_id = chat.id
                    inner join link on subscription.link_id = link.id
                where
                    chat.tg_id = ?
                    and link.url = ?""")
            .param(tgId)
            .param(url.toString())
            .query(Subscription.class)
            .optional();
    }

    @Override
    public Subscription add(Subscription subscription) {
        client.sql("insert into subscription (chat_id, link_id) values (?, ?)")
            .param(subscription.chatId())
            .param(subscription.linkId())
            .update();
        return subscription;
    }

    @Override
    public void remove(Subscription subscription) {
        client.sql("delete from subscription where chat_id = ? and link_id = ?")
            .param(subscription.chatId())
            .param(subscription.linkId())
            .update();
    }

    @Override
    public List<Subscription> findAll() {
        return client.sql("select * from subscription")
            .query(Subscription.class)
            .list();
    }

    @Override
    public List<Subscription> findAllByChat(long chatId) {
        return client.sql("select * from subscription where chat_id = ?")
            .param(chatId)
            .query(Subscription.class)
            .list();
    }

    @Override
    public boolean linkNotFollowedByAnyone(long linkId) {
        List<Subscription> subscriptions = client.sql("select * from subscription where link_id = ?")
            .param(linkId)
            .query(Subscription.class)
            .list();
        return subscriptions.isEmpty();
    }

    @Override
    public List<Link> findAllLinksByChat(Chat chat) {
        return client.sql("""
                select link.*
                from link
                    inner join subscription on link.id = subscription.link_id
                where
                    subscription.chat_id = ?""")
            .param(chat.id())
            .query(Link.class)
            .list();
    }
}
