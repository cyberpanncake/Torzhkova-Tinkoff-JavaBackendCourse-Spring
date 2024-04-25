package edu.java.dto.api.scrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiErrorResponse(
    String description,
    String code,
    @JsonProperty("exception_name")
    String exceptionName,
    @JsonProperty("exception_message")
    String exceptionMessage,
    String[] stacktrace
) {
}
