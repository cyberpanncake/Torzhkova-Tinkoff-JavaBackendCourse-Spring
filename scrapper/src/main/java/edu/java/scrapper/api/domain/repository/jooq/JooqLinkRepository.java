package edu.java.scrapper.api.domain.repository.jooq;

import edu.java.scrapper.api.domain.dto.Link;
import edu.java.scrapper.api.domain.jooq.Tables;
import edu.java.scrapper.api.domain.repository.LinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class JooqLinkRepository extends AbstractJooqRepository implements LinkRepository {

    public JooqLinkRepository(DSLContext dslContext) {
        super(dslContext);
    }

    @Override
    public Optional<Link> findById(long id) {
        return dslContext.selectFrom(Tables.LINK)
            .where(Tables.LINK.ID.eq(id))
            .fetchOptional()
            .map(r -> r.into(Link.class));
    }

    @Override
    public Optional<Link> findByUrl(URI url) {
        return dslContext.selectFrom(Tables.LINK)
            .where(Tables.LINK.URL.eq(url.toString()))
            .fetchOptional()
            .map(r -> r.into(Link.class));
    }

    @Override
    public Link add(Link link) {
        return dslContext
            .insertInto(Tables.LINK)
            .set(Tables.LINK.URL, link.url().toString())
            .set(Tables.LINK.LAST_UPDATE, link.lastUpdate())
            .set(Tables.LINK.LAST_CHECK, link.lastCheck())
            .returning()
            .fetchOptional()
            .map(r -> r.into(Link.class))
            .get();
    }

    @Override
    public void remove(URI url) {
        dslContext
            .deleteFrom(Tables.LINK)
            .where(Tables.LINK.URL.eq(url.toString()))
            .execute();
    }

    @Override
    public void remove(long id) {
        dslContext
            .deleteFrom(Tables.LINK)
            .where(Tables.LINK.ID.eq(id))
            .execute();
    }

    @Override
    public List<Link> findAll() {
        return dslContext
            .selectFrom(Tables.LINK)
            .fetchInto(Link.class);
    }

    @Override
    public List<Link> findAllWithLastCheckOlderThan(OffsetDateTime time) {
        return dslContext.selectFrom(Tables.LINK)
            .where(Tables.LINK.LAST_CHECK.lessThan(time))
            .fetchInto(Link.class);
    }

    @Override
    public void update(Link link) {
        dslContext
            .update(Tables.LINK)
            .set(Tables.LINK.LAST_UPDATE, link.lastUpdate())
            .set(Tables.LINK.LAST_CHECK, link.lastCheck())
            .where(Tables.LINK.ID.eq(link.id()))
            .execute();
    }
}
