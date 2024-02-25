package edu.java.client;

import edu.java.dto.StackoverflowResponse;
import java.util.Optional;
import java.util.Properties;

public class StackoverflowClient extends AbstractClient {
    private static final String DEFAULT_URL = new Properties().getProperty("app.base-url.stackoverflow");

    public StackoverflowClient() {
        super(DEFAULT_URL);
    }

    public StackoverflowClient(String baseUrl) {
        super(baseUrl);
    }

    public Optional<StackoverflowResponse> getUpdate(Long questionId) {
        String request = String.format("questions/%d/answers", questionId);
        return Optional.ofNullable(client.get()
            .uri(uriBuilder -> uriBuilder
                .path(request)
                .queryParam("site", "stackoverflow")
                .queryParam("sort", "activity")
                .queryParam("order", "desc")
                .queryParam("pagesize", 1)
                .build())
            .retrieve()
            .bodyToMono(StackoverflowResponse.class)
            .block()
        );
    }
}
