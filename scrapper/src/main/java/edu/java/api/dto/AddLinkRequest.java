package edu.java.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.URL;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AddLinkRequest(
    @URL
    String link
) {
}
