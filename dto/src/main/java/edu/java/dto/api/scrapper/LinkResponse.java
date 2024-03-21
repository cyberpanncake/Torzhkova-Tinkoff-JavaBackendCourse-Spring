package edu.java.dto.api.scrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.net.URI;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LinkResponse(
    Long id,
    URI url
) {
}
