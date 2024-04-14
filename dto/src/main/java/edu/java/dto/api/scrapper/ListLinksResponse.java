package edu.java.dto.api.scrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ListLinksResponse(
    LinkResponse[] links,
    Integer size
) {
}
