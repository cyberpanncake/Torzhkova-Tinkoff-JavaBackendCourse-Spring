package edu.java.scrapper.api.domain.repository.jdbc;

import org.springframework.jdbc.core.simple.JdbcClient;

public abstract class AbstractJdbcRepository {
    protected final JdbcClient client;

    public AbstractJdbcRepository(JdbcClient client) {
        this.client = client;
    }
}
