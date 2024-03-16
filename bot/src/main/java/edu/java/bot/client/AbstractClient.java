package edu.java.bot.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class AbstractClient {
    protected WebClient client;
    protected final ObjectMapper mapper;

    public AbstractClient(@NonNull String baseUrl, ObjectMapper mapper) {
        this.client = WebClient.builder().baseUrl(baseUrl).build();
        this.mapper = mapper;
    }
}
