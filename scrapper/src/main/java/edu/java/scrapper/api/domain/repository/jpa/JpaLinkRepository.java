package edu.java.scrapper.api.domain.repository.jpa;

import edu.java.scrapper.api.domain.dto.jpa.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface JpaLinkRepository extends JpaRepository<Link, Long> {
}