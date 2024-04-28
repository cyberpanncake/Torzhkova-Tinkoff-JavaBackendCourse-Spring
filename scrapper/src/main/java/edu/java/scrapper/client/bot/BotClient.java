package edu.java.scrapper.client.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.dto.api.bot.ApiErrorResponse;
import edu.java.dto.api.bot.LinkUpdateRequest;
import edu.java.dto.api.exception.BotApiException;
import edu.java.scrapper.api.service.updates.UpdateSender;
import edu.java.scrapper.client.AbstractClient;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
public class BotClient extends AbstractClient implements UpdateSender {

    public BotClient(@NonNull String baseUrl, ObjectMapper mapper, Retry retry) {
        super(baseUrl, mapper, retry);
    }

    public void send(LinkUpdateRequest request) {
        try {
            client.post()
                .uri("/updates")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), LinkUpdateRequest.class)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::getException)
                .bodyToMono(Void.class)
                .retryWhen(retry)
                .block();
        } catch (WebClientRequestException e) {
            log.error("Приложение бота недоступно");
        }
    }

    private Mono<BotApiException> getException(ClientResponse response) {
        return response
            .bodyToMono(ApiErrorResponse.class)
            .map(BotApiException::new);
    }
}
