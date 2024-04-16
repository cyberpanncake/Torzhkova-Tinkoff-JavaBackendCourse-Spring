package edu.java.bot.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

public abstract class AbstractClient {
    protected WebClient client;
    protected final ObjectMapper mapper;
    protected final Retry retry;

    public AbstractClient(@NonNull String baseUrl, ObjectMapper mapper, Retry retry) {
        this.client = WebClient.builder().baseUrl(baseUrl).build();
        this.mapper = mapper;
        this.retry = retry;
    }
}
