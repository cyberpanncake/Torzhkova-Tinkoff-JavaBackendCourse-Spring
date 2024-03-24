package edu.java.scrapper.client.sources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.scrapper.client.AbstractClient;
import edu.java.scrapper.dto.StackoverflowResponse;
import java.util.Optional;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public class StackoverflowClient extends AbstractClient {

    public StackoverflowClient(String baseUrl, ObjectMapper mapper) {
        super(baseUrl, mapper);
    }

    public Optional<StackoverflowResponse> getUpdate(long questionId) throws ResponseException {
        Mono<JsonNode> jsonNodeMono = client.get()
            .uri(uriBuilder -> uriBuilder
                .path("/questions/{questionId}/answers")
                .queryParam("site", "stackoverflow")
                .queryParam("sort", "activity")
                .queryParam("order", "desc")
                .queryParam("pagesize", 1)
                .build(questionId))
            .retrieve()
            .bodyToMono(JsonNode.class);
        try {
            JsonNode root = jsonNodeMono.block();
            JsonNode update = root.get("items").get(0);
            return Optional.ofNullable(mapper.treeToValue(update, StackoverflowResponse.class));
        } catch (WebClientResponseException | NullPointerException | JsonProcessingException e) {
            throw new ResponseException();
        }
    }
}
