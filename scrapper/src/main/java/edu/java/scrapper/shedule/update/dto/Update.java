package edu.java.scrapper.shedule.update.dto;

import java.time.OffsetDateTime;
import lombok.Getter;

@Getter
public class Update {
    protected final OffsetDateTime createdAt;
    protected final String details;

    public Update(OffsetDateTime createdAt, String details) {
        this.createdAt = createdAt;
        this.details = details;
    }
}
