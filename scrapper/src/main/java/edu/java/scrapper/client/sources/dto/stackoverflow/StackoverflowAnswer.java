package edu.java.scrapper.client.sources.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.java.scrapper.client.sources.dto.SourceUpdate;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StackoverflowAnswer(
    Owner owner,
    OffsetDateTime lastActivityDate,
    Long answerId,
    Long questionId
) implements SourceUpdate {

    @Override
    public String getDetailsDescription() {
        return "%s ответил(-а) на вопрос".formatted(owner.displayName);
    }

    @Override
    public OffsetDateTime getDate() {
        return lastActivityDate;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Owner(
        String displayName
    ) {
    }
}
