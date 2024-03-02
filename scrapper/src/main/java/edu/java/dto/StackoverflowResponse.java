package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StackoverflowResponse(
    Owner owner,
    @SuppressWarnings("RecordComponentName")
    OffsetDateTime last_activity_date,
    @SuppressWarnings("RecordComponentName")
    Long answer_id,
    @SuppressWarnings("RecordComponentName")
    Long question_id
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Owner(
        @SuppressWarnings("RecordComponentName")
        String display_name
    ) {
    }
}
