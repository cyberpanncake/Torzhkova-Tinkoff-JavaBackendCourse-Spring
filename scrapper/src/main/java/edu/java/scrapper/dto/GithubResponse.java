package edu.java.scrapper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GithubResponse(
    Long id,
    String type,
    Actor actor,
    Repository repo,
    OffsetDateTime createdAt
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Actor(
        String displayLogin
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Repository(String name) {
    }
}
