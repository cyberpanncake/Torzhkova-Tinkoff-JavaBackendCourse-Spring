package edu.java.client;

import edu.java.dto.GithubResponse;
import java.util.Optional;
import java.util.Properties;

public class GithubClient extends AbstractClient {
    private static final String DEFAULT_URL = new Properties().getProperty("app.base-url.github");

    public GithubClient() {
        super(DEFAULT_URL);
    }

    public GithubClient(String baseUrl) {
        super(baseUrl);
    }

    public Optional<GithubResponse> getUpdate(String author, String repository) {
        String request = String.format("networks/%s/%s/events", author, repository);
        return Optional.ofNullable(client.get()
            .uri(uriBuilder -> uriBuilder
                .path(request)
                .queryParam("per_page", 1)
                .build())
            .retrieve()
            .bodyToMono(GithubResponse.class)
            .block()
        );
    }
}
