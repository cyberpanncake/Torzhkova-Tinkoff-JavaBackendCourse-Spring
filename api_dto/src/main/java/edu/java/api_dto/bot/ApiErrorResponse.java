package edu.java.api_dto.bot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiErrorResponse(
    String description,
    String code,
    String exceptionName,
    String exceptionMessage,
    String[] stacktrace
) {
}
