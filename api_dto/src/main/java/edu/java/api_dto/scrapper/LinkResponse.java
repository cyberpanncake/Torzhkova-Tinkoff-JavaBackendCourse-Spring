package edu.java.api_dto.scrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.URL;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LinkResponse(
    Long id,
    @URL
    String url
) {
}
