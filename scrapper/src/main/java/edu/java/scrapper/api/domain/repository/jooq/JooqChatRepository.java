package edu.java.scrapper.api.domain.repository.jooq;

import edu.java.scrapper.api.domain.dto.Chat;
import edu.java.scrapper.api.domain.jooq.Tables;
import edu.java.scrapper.api.domain.repository.ChatRepository;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class JooqChatRepository extends AbstractJooqRepository implements ChatRepository {

    public JooqChatRepository(DSLContext dslContext) {
        super(dslContext);
    }

    @Override
    public Optional<Chat> findByTgId(long tgId) {
        return dslContext.selectFrom(Tables.CHAT)
            .where(Tables.CHAT.TG_ID.eq(tgId))
            .fetchOptional()
            .map(r -> r.into(Chat.class));
    }

    @Override
    public Chat add(long tgId) {
        return dslContext
            .insertInto(Tables.CHAT)
            .set(Tables.CHAT.TG_ID, tgId)
            .returning()
            .fetchOptional()
            .map(r -> r.into(Chat.class))
            .get();
    }

    @Override
    public void remove(long tgId) {
        dslContext
            .deleteFrom(Tables.CHAT)
            .where(Tables.CHAT.TG_ID.eq(tgId))
            .execute();
    }

    @Override
    public List<Chat> findAll() {
        return dslContext
            .selectFrom(Tables.CHAT)
            .fetchInto(Chat.class);
    }
}
