package edu.java.bot.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.net.URI;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LinkUpdateRequest(
    int id,
    URI url,
    String description,
    int[] tgChatIds
) {
}
