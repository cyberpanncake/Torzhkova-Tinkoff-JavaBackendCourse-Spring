package edu.java.scrapper.client.sources.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.java.scrapper.client.sources.dto.SourceUpdate;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StackoverflowComment(
    Owner owner,
    OffsetDateTime creationDate
) implements SourceUpdate {

    @Override
    public String getDetailsDescription() {
        return "%s прокомментировал(-а) вопрос".formatted(owner.displayName);
    }

    @Override
    public OffsetDateTime getDate() {
        return creationDate;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Owner(
        String displayName
    ) {
    }
}
