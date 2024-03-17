package edu.java.scrapper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StackoverflowResponse(
    Owner owner,
    OffsetDateTime lastActivityDate,
    Long answerId,
    Long questionId
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Owner(
        String displayName
    ) {
    }
}
