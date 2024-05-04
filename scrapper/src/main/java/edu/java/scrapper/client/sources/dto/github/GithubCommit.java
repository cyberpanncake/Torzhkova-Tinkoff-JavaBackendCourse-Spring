package edu.java.scrapper.client.sources.dto.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.java.scrapper.client.sources.dto.SourceUpdate;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GithubCommit(
    Author author,
    CommitNode commit
) implements SourceUpdate {

    @Override
    public String getDetailsDescription() {
        return "%s добавил(-а) новый коммит с сообщением \"%s\"".formatted(author.login, commit.message);
    }

    @Override
    public OffsetDateTime getDate() {
        return commit.author.date;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Author(
        String login
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CommitNode(
        CommitNodeAuthor author,

        String message
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CommitNodeAuthor(
        OffsetDateTime date
    ) {
    }
}
