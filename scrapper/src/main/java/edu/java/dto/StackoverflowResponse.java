package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StackoverflowResponse(
    Owner owner,
    @JsonProperty("last_activity_date")
    OffsetDateTime lastActivityDate,
    @JsonProperty("answer_id")
    Long answerId,
    @JsonProperty("question_id")
    Long questionId
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Owner(
        @JsonProperty("display_name")
        String displayName
    ) {
    }
}
