package edu.java.scrapper.api.domain.repository.jdbc;

import edu.java.scrapper.api.domain.dto.Link;
import edu.java.scrapper.api.domain.repository.LinkRepository;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class JdbcLinkRepository extends AbstractJdbcRepository implements LinkRepository {

    public JdbcLinkRepository(JdbcClient client) {
        super(client);
    }

    @Override
    public Optional<Link> findById(long id) {
        return client.sql("select * from link where id = ?")
            .param(id)
            .query(Link.class)
            .optional();
    }

    @Override
    public Optional<Link> findByUrl(URI url) {
        return client.sql("select * from link where url = ?")
            .param(url.toString())
            .query(Link.class)
            .optional();
    }

    @Override
    public Link add(Link link) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        client.sql("insert into link (url, last_update, last_check) values (?, ?, ?)")
            .param(link.url().toString())
            .param(link.lastUpdate())
            .param(link.lastCheck())
            .update(keyHolder, "id");
        long id = keyHolder.getKey().longValue();
        return new Link(id, link.url(), link.lastUpdate(), link.lastCheck());
    }

    @Override
    public void remove(URI url) {
        client.sql("delete from link where url = ?")
            .param(url.toString())
            .update();
    }

    @Override
    public void remove(long id) {
        client.sql("delete from link where id = ?")
            .param(id)
            .update();
    }

    @Override
    public List<Link> findAll() {
        return client.sql("select * from link")
            .query(Link.class)
            .list();
    }
}
