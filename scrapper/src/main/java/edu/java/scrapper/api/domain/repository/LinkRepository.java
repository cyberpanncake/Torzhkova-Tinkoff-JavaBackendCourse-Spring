package edu.java.scrapper.api.domain.repository;

import edu.java.scrapper.api.domain.dto.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    Optional<Link> findById(long id);

    Optional<Link> findByUrl(URI url);

    Link add(Link link);

    void remove(URI url);

    void remove(long id);

    List<Link> findAll();

    List<Link> findAllWithLastCheckOlderThan(OffsetDateTime time);

    void update(Link link);
}
