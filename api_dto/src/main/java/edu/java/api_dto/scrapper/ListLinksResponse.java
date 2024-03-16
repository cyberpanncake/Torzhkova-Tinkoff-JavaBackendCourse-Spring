package edu.java.api_dto.scrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ListLinksResponse(
    LinkResponse[] links,
    Integer size
) {
}
