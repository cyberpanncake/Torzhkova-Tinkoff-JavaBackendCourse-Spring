package edu.java.dto.api.bot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LinkUpdateRequest(
    Long id,
    URI url,
    String description,
    @JsonProperty("tg_chat_ids")
    Long[] tgChatIds
) {
}

