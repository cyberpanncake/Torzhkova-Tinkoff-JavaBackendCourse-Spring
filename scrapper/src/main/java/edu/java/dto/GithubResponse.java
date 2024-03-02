package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GithubResponse(
    Long id,
    String type,
    Actor actor,
    Repository repo,
    @SuppressWarnings("RecordComponentName")
    OffsetDateTime created_at
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Actor(
        @SuppressWarnings("RecordComponentName")
        String display_login
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Repository(String name) {
    }
}
