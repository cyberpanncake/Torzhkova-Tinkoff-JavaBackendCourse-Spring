package edu.java.api_dto.bot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.URL;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LinkUpdateRequest(
    Long id,
    @URL
    String url,
    String description,
    Long[] tgChatIds
) {
}

