package edu.java.client.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.api_dto.bot.ApiErrorResponse;
import edu.java.api_dto.bot.LinkUpdateRequest;
import edu.java.client.AbstractClient;
import lombok.NonNull;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public class BotClient extends AbstractClient {

    public BotClient(@NonNull String baseUrl, ObjectMapper mapper) {
        super(baseUrl, mapper);
    }

    public void sendUpdate(LinkUpdateRequest request) {
        client.post()
            .uri("/update")
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), LinkUpdateRequest.class)
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::getException)
            .bodyToMono(Void.class)
            .block();
    }

    private Mono<BotApiException> getException(ClientResponse response) {
        return response
            .bodyToMono(ApiErrorResponse.class)
            .map(BotApiException::new);
    }
}
