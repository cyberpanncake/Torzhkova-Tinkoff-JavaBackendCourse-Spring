package edu.java.scrapper.client.sources.dto.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.java.scrapper.client.sources.dto.SourceUpdate;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GithubEvent(
    Long id,
    String type,
    Actor actor,
    Repository repo,
    OffsetDateTime createdAt
) implements SourceUpdate {

    @Override
    public String getDetailsDescription() {
        return "В репозитории %s/%s произошло событие типа %s".formatted(repo.name, actor.displayLogin, type);
    }

    @Override
    public OffsetDateTime getDate() {
        return createdAt;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Actor(
        String displayLogin
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Repository(String name) {
    }
}
