package edu.java.scrapper.api.domain.repository.jooq;

import edu.java.scrapper.api.domain.dto.Chat;
import edu.java.scrapper.api.domain.dto.Link;
import edu.java.scrapper.api.domain.dto.Subscription;
import edu.java.scrapper.api.domain.jooq.Tables;
import edu.java.scrapper.api.domain.repository.SubscriptionRepository;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class JooqSubscriptionRepository extends AbstractJooqRepository implements SubscriptionRepository {

    public JooqSubscriptionRepository(DSLContext dslContext) {
        super(dslContext);
    }

    @Override
    public Optional<Subscription> find(long tgId, URI url) {
        return dslContext
            .select(Tables.SUBSCRIPTION.fields())
            .from(Tables.SUBSCRIPTION)
            .innerJoin(Tables.CHAT).on(
                Tables.CHAT.ID.eq(Tables.SUBSCRIPTION.CHAT_ID))
            .innerJoin(Tables.LINK).on(
                Tables.LINK.ID.eq(Tables.SUBSCRIPTION.LINK_ID))
            .where(Tables.CHAT.TG_ID.eq(tgId).and(
                Tables.LINK.URL.eq(url.toString())
            ))
            .fetchOptional()
            .map(r -> r.into(Subscription.class));
    }

    @Override
    public Subscription add(Subscription subscription) {
        return dslContext
            .insertInto(Tables.SUBSCRIPTION)
            .set(Tables.SUBSCRIPTION.CHAT_ID, subscription.chatId())
            .set(Tables.SUBSCRIPTION.LINK_ID, subscription.linkId())
            .returning()
            .fetchOptional()
            .map(r -> r.into(Subscription.class))
            .get();
    }

    @Override
    public void remove(Subscription subscription) {
        dslContext
            .deleteFrom(Tables.SUBSCRIPTION)
            .where(Tables.SUBSCRIPTION.CHAT_ID.eq(subscription.chatId()).and(
                Tables.SUBSCRIPTION.LINK_ID.eq(subscription.linkId())
            ))
            .execute();
    }

    @Override
    public List<Subscription> findAll() {
        return dslContext
            .selectFrom(Tables.SUBSCRIPTION)
            .fetchInto(Subscription.class);
    }

    @Override
    public List<Subscription> findAllByChat(Chat chat) {
        return dslContext
            .selectFrom(Tables.SUBSCRIPTION)
            .where(Tables.SUBSCRIPTION.CHAT_ID.eq(chat.id()))
            .fetchInto(Subscription.class);
    }

    @SuppressWarnings("MultipleStringLiterals")
    @Override
    public List<Subscription> findAllByLink(Link link) {
        return dslContext
            .selectFrom(Tables.SUBSCRIPTION)
            .where(Tables.SUBSCRIPTION.LINK_ID.eq(link.id()))
            .fetchInto(Subscription.class);
    }

    @SuppressWarnings("MultipleStringLiterals")
    @Override
    public boolean linkNotFollowedByAnyone(long linkId) {
        return dslContext
            .selectFrom(Tables.SUBSCRIPTION)
            .where(Tables.SUBSCRIPTION.LINK_ID.eq(linkId))
            .fetchInto(Subscription.class).isEmpty();
    }

    @Override
    public List<Link> findAllLinksByChat(Chat chat) {
        return dslContext
            .select(Tables.LINK.fields())
            .from(Tables.LINK)
            .innerJoin(Tables.SUBSCRIPTION).on(
                Tables.LINK.ID.eq(Tables.SUBSCRIPTION.LINK_ID))
            .where(Tables.SUBSCRIPTION.CHAT_ID.eq(chat.id()))
            .fetchInto(Link.class);
    }

    @Override
    public List<Chat> findAllChatsByLink(Link link) {
        return dslContext
            .select(Tables.CHAT.fields())
            .from(Tables.CHAT)
            .innerJoin(Tables.SUBSCRIPTION).on(
                Tables.CHAT.ID.eq(Tables.SUBSCRIPTION.CHAT_ID))
            .where(Tables.SUBSCRIPTION.LINK_ID.eq(link.id()))
            .fetchInto(Chat.class);
    }
}
