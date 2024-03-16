package edu.java.bot.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;

public class ScrapperClient extends AbstractClient {
    public ScrapperClient(@NonNull String baseUrl, ObjectMapper mapper) {
        super(baseUrl, mapper);
    }
}
