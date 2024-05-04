package edu.java.scrapper.client.sources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.dto.api.exception.ApiException;
import edu.java.scrapper.client.AbstractClient;
import edu.java.scrapper.client.sources.dto.SourceUpdate;
import edu.java.scrapper.client.sources.dto.github.GithubCommit;
import edu.java.scrapper.client.sources.dto.github.GithubEvent;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class GithubClient extends AbstractClient {
    private static final String PER_PAGE = "per_page";

    public GithubClient(String baseUrl, ObjectMapper mapper, Retry retry) {
        super(baseUrl, mapper, retry);
    }

    public Optional<SourceUpdate> getUpdate(String owner, String repo) throws ResponseException {
        List<Optional<SourceUpdate>> updates = List.of(
            getLastEvent(owner, repo),
            getLastCommit(owner, repo)
        );
        return updates.stream().filter(Optional::isPresent).map(Optional::get)
            .max(Comparator.comparing(SourceUpdate::getDate));
    }

    public Optional<SourceUpdate> getLastEvent(String owner, String repo) throws ResponseException {
        Mono<JsonNode> jsonNodeMono = client.get()
            .uri(uriBuilder -> uriBuilder
                .path("/repos/{owner}/{repo}/events")
                .queryParam(PER_PAGE, 1)
                .build(owner, repo))
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::getException)
            .bodyToMono(JsonNode.class)
            .retryWhen(retry);
        try {
            JsonNode root = jsonNodeMono.block();
            JsonNode update = root.get(0);
            return Optional.ofNullable(mapper.treeToValue(update, GithubEvent.class));
        } catch (WebClientResponseException | NullPointerException | JsonProcessingException | ApiException e) {
            throw new ResponseException();
        }
    }

    public Optional<SourceUpdate> getLastCommit(String owner, String repo) throws ResponseException {
        Mono<JsonNode> jsonNodeMono = client.get()
            .uri(uriBuilder -> uriBuilder
                .path("/repos/{owner}/{repo}/commits")
                .queryParam(PER_PAGE, 1)
                .build(owner, repo))
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::getException)
            .bodyToMono(JsonNode.class)
            .retryWhen(retry);
        try {
            JsonNode root = jsonNodeMono.block();
            JsonNode update = root.get(0);
            return Optional.ofNullable(mapper.treeToValue(update, GithubCommit.class));
        } catch (WebClientResponseException | NullPointerException | JsonProcessingException | ApiException e) {
            throw new ResponseException();
        }
    }

    private Mono<ApiException> getException(ClientResponse response) {
        return Mono.error(new ApiException(response.statusCode().toString(), ""));
    }
}
