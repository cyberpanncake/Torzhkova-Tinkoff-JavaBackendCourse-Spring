package edu.java.scrapper.client.sources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.dto.api.exception.ApiException;
import edu.java.scrapper.client.AbstractClient;
import edu.java.scrapper.client.sources.dto.SourceUpdate;
import edu.java.scrapper.client.sources.dto.stackoverflow.StackoverflowAnswer;
import edu.java.scrapper.client.sources.dto.stackoverflow.StackoverflowComment;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class StackoverflowClient extends AbstractClient {
    private static final String SITE_KEY = "site";
    private static final String SITE_VALUE = "stackoverflow";
    private static final String PAGESIZE = "pagesize";
    private static final String ITEMS = "items";


    public StackoverflowClient(String baseUrl, ObjectMapper mapper, Retry retry) {
        super(baseUrl, mapper, retry);
    }

    public Optional<SourceUpdate> getUpdate(long questionId) throws ResponseException {
        List<Optional<SourceUpdate>> updates = List.of(
            getLastAnswer(questionId),
            getLastComment(questionId)
        );
        return updates.stream().filter(Optional::isPresent).map(Optional::get)
            .max(Comparator.comparing(SourceUpdate::getDate));
    }

    private Optional<SourceUpdate> getLastAnswer(long questionId) throws ResponseException {
        Mono<JsonNode> jsonNodeMono = client.get()
            .uri(uriBuilder -> uriBuilder
                .path("/questions/{questionId}/answers")
                .queryParam(SITE_KEY, SITE_VALUE)
                .queryParam("sort", "activity")
                .queryParam("order", "desc")
                .queryParam(PAGESIZE, 1)
                .build(questionId))
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::getException)
            .bodyToMono(JsonNode.class)
            .retryWhen(retry);
        try {
            JsonNode root = jsonNodeMono.block();
            JsonNode update = root.get(ITEMS).get(0);
            return Optional.ofNullable(mapper.treeToValue(update, StackoverflowAnswer.class));
        } catch (WebClientResponseException | NullPointerException | JsonProcessingException | ApiException e) {
            throw new ResponseException();
        }
    }

    private Optional<SourceUpdate> getLastComment(long questionId) throws ResponseException {
        Mono<JsonNode> jsonNodeMono = client.get()
            .uri(uriBuilder -> uriBuilder
                .path("/questions/{questionId}/comments")
                .queryParam(SITE_KEY, SITE_VALUE)
                .queryParam(PAGESIZE, 1)
                .build(questionId))
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::getException)
            .bodyToMono(JsonNode.class)
            .retryWhen(retry);
        try {
            JsonNode root = jsonNodeMono.block();
            JsonNode update = root.get(ITEMS).get(0);
            return Optional.ofNullable(mapper.treeToValue(update, StackoverflowComment.class));
        } catch (WebClientResponseException | NullPointerException | JsonProcessingException | ApiException e) {
            throw new ResponseException();
        }
    }

    private Mono<ApiException> getException(ClientResponse response) {
        return Mono.error(new ApiException(response.statusCode().toString(), ""));
    }
}
