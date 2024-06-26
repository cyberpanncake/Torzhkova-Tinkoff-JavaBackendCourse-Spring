package edu.java.dto.api.scrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.URL;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RemoveLinkRequest(
    @URL
    String link
) {
}
