package edu.java.client.sources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.client.AbstractClient;
import edu.java.dto.GithubResponse;
import java.util.Optional;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public class GithubClient extends AbstractClient {

    public GithubClient(String baseUrl, ObjectMapper mapper) {
        super(baseUrl, mapper);
    }

    public Optional<GithubResponse> getUpdate(String author, String repository) throws ResponseException {
        Mono<JsonNode> jsonNodeMono = client.get()
            .uri(uriBuilder -> uriBuilder
                .path("/repos/{author}/{repo}/events")
                .queryParam("per_page", 1)
                .build(author, repository))
            .retrieve()
            .bodyToMono(JsonNode.class);
        try {
            JsonNode root = jsonNodeMono.block();
            JsonNode update = root.get(0);
            return Optional.ofNullable(mapper.treeToValue(update, GithubResponse.class));
        } catch (WebClientResponseException | NullPointerException | JsonProcessingException e) {
            throw new ResponseException();
        }
    }
}
