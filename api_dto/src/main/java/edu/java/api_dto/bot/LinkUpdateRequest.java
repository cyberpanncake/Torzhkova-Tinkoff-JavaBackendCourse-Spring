package edu.java.api_dto.bot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.net.URI;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LinkUpdateRequest(
    Long id,
    URI url,
    String description,
    Long[] tgChatIds
) {
}

