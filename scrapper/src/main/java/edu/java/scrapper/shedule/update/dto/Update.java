package edu.java.scrapper.shedule.update.dto;

import java.time.OffsetDateTime;
import lombok.Getter;

@Getter
public class Update {
    protected final OffsetDateTime createdAt;

    public Update(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
