package edu.java.scrapper.client.sources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.scrapper.client.AbstractClient;
import edu.java.scrapper.client.sources.dto.GithubResponse;
import java.util.Optional;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public class GithubClient extends AbstractClient {

    public GithubClient(String baseUrl, ObjectMapper mapper) {
        super(baseUrl, mapper);
    }

    public Optional<GithubResponse> getUpdate(String owner, String repo) throws ResponseException {
        Mono<JsonNode> jsonNodeMono = client.get()
            .uri(uriBuilder -> uriBuilder
                .path("/repos/{owner}/{repo}/events")
                .queryParam("per_page", 1)
                .build(owner, repo))
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
