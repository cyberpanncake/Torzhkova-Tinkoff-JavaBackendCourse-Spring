package edu.java.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.java.dto.StackoverflowResponse;
import java.util.Optional;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public class StackoverflowClient extends AbstractClient {
    private final static ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.registerModule(new JavaTimeModule());
    }

    public StackoverflowClient(String baseUrl) {
        super(baseUrl);
    }

    public Optional<StackoverflowResponse> getUpdate(long questionId) {
        String request = String.format("questions/%d/answers", questionId);
        Mono<JsonNode> jsonNodeMono = client.get()
            .uri(uriBuilder -> uriBuilder
                .path(request)
                .queryParam("site", "stackoverflow")
                .queryParam("sort", "activity")
                .queryParam("order", "desc")
                .queryParam("pagesize", 1)
                .build())
            .retrieve()
            .bodyToMono(JsonNode.class);
        try {
            JsonNode root = jsonNodeMono.block();
            JsonNode update = root.get("items").get(0);
            return Optional.ofNullable(MAPPER.treeToValue(update, StackoverflowResponse.class));
        } catch (WebClientResponseException | NullPointerException | JsonProcessingException e) {
            return null;
        }
    }
}
