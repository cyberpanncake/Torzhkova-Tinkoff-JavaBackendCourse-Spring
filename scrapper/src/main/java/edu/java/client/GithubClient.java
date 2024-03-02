package edu.java.client;

import edu.java.dto.GithubResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class GithubClient extends AbstractClient {

    public GithubClient(String baseUrl) {
        super(baseUrl);
    }

    public Optional<GithubResponse> getUpdate(String author, String repository) throws ResponseException {
        try {
            GithubResponse[] responses = client.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/repos/{author}/{repo}/events")
                    .queryParam("per_page", 1)
                    .build(author, repository))
                .retrieve()
                .bodyToMono(GithubResponse[].class)
                .block();
            return Stream.ofNullable(responses)
                .flatMap(Arrays::stream)
                .findFirst();
        } catch (WebClientResponseException e) {
            throw new ResponseException();
        }
    }
}
