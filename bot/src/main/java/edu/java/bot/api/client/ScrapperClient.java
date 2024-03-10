package edu.java.bot.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;

public class ScrapperClient extends AbstractClient {
    public ScrapperClient(@NonNull String baseUrl, ObjectMapper mapper) {
        super(baseUrl, mapper);
    }
}
