package edu.java.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ListLinksResponse(
    LinkResponse[] links,
    int size
) {
}
