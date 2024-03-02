package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GithubResponse(
    Long id,
    String type,
    Actor actor,
    Repository repo,
    @JsonProperty("created_at")
    OffsetDateTime createdAt
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Actor(
        @JsonProperty("display_login")
        String displayLogin
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Repository(String name) {
    }
}
