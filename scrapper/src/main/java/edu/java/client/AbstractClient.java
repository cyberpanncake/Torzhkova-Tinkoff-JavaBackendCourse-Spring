package edu.java.client;

import org.springframework.web.reactive.function.client.WebClient;

public abstract class AbstractClient {
    protected WebClient client;

    public AbstractClient(String baseUrl) {
        if (baseUrl.isEmpty()) {
            throw new IllegalArgumentException("Укажите url отслеживаемого ресурса");
        }
        this.client = WebClient.builder().baseUrl(baseUrl).build();
    }
}
