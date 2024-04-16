package edu.java.scrapper.client.sources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.dto.api.exception.ApiException;
import edu.java.scrapper.client.AbstractClient;
import edu.java.scrapper.client.sources.dto.GithubResponse;
import java.util.Optional;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class GithubClient extends AbstractClient {

    public GithubClient(String baseUrl, ObjectMapper mapper, Retry retry) {
        super(baseUrl, mapper, retry);
    }

    public Optional<GithubResponse> getUpdate(String owner, String repo) throws ResponseException {
        Mono<JsonNode> jsonNodeMono = client.get()
            .uri(uriBuilder -> uriBuilder
                .path("/repos/{owner}/{repo}/events")
                .queryParam("per_page", 1)
                .build(owner, repo))
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::getException)
            .bodyToMono(JsonNode.class)
            .retryWhen(retry);
        try {
            JsonNode root = jsonNodeMono.block();
            JsonNode update = root.get(0);
            return Optional.ofNullable(mapper.treeToValue(update, GithubResponse.class));
        } catch (WebClientResponseException | NullPointerException | JsonProcessingException | ApiException e) {
            throw new ResponseException();
        }
    }

    private Mono<ApiException> getException(ClientResponse response) {
        return Mono.error(new ApiException(response.statusCode().toString(), ""));
    }
}
