package edu.java.client;

import edu.java.dto.GithubResponse;
import java.util.Optional;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class GithubClient extends AbstractClient {

    public GithubClient(String baseUrl) {
        super(baseUrl);
    }

    public Optional<GithubResponse> getUpdate(String author, String repository) {
        String request = String.format("networks/%s/%s/events", author, repository);
        try {
            GithubResponse[] responses = client.get()
                .uri(uriBuilder -> uriBuilder
                    .path(request)
                    .queryParam("per_page", 1)
                    .build())
                .retrieve()
                .bodyToMono(GithubResponse[].class)
                .block();
            if (responses == null || responses.length == 0) {
                return Optional.empty();
            }
            return Optional.of(responses[0]);
        } catch (WebClientResponseException e) {
            return null;
        }
    }
}
