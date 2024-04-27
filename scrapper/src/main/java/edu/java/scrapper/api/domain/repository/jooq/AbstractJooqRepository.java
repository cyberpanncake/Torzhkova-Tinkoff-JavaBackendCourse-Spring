package edu.java.scrapper.api.domain.repository.jooq;

import org.jooq.DSLContext;

public abstract class AbstractJooqRepository {
    protected final DSLContext dslContext;

    public AbstractJooqRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }
}
