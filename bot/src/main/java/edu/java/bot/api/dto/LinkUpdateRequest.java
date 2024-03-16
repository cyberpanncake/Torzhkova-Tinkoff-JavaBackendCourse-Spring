package edu.java.bot.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.URL;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LinkUpdateRequest(
    long id,
    @URL
    String url,
    String description,
    long[] tgChatIds
) {
}
