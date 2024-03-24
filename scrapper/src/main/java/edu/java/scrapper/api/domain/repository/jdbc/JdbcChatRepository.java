package edu.java.scrapper.api.domain.repository.jdbc;

import edu.java.scrapper.api.domain.dto.Chat;
import edu.java.scrapper.api.domain.repository.ChatRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class JdbcChatRepository extends AbstractJdbcRepository implements ChatRepository {

    public JdbcChatRepository(JdbcClient client) {
        super(client);
    }

    @Override
    public Optional<Chat> findByTgId(long tgId) {
        return client.sql("select * from chat where tg_id = ?")
            .param(tgId)
            .query(Chat.class)
            .optional();
    }

    @Override
    public Chat add(long tgId) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        client.sql("insert into chat (tg_id) values (?)")
            .param(tgId)
            .update(keyHolder, "id");
        long id = keyHolder.getKey().longValue();
        return new Chat(id, tgId);
    }

    @Override
    public void remove(long tgId) {
        client.sql("delete from chat where tg_id = ?")
            .param(tgId)
            .update();
    }

    @Override
    public List<Chat> findAll() {
        return client.sql("select * from chat")
            .query(Chat.class)
            .list();
    }
}
