package edu.java.bot.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.dto.api.scrapper.AddLinkRequest;
import edu.java.dto.api.scrapper.ApiErrorResponse;
import edu.java.dto.api.scrapper.LinkResponse;
import edu.java.dto.api.scrapper.ListLinksResponse;
import edu.java.dto.api.scrapper.RemoveLinkRequest;
import lombok.NonNull;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public class ScrapperClient extends AbstractClient {
    private static final String CHAT_BASE_URL = "/tg-chat";
    private static final String LINK_BASE_URL = "/links";
    private static final String ID_PATH = "/{id}";
    private static final String CHAT_HEADER = "Tg-Chat-Id";

    public ScrapperClient(@NonNull String baseUrl, ObjectMapper mapper) {
        super(baseUrl, mapper);
    }

    public void registerChat(long id) throws ScrapperApiException {
        client.post()
            .uri(uriBuilder -> uriBuilder
                .path(CHAT_BASE_URL + ID_PATH)
                .build(id))
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::getException)
            .bodyToMono(Void.class)
            .block();
    }

    public void deleteChat(long id) throws ScrapperApiException {
        client.delete()
            .uri(uriBuilder -> uriBuilder
                .path(CHAT_BASE_URL + ID_PATH)
                .build(id))
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::getException)
            .bodyToMono(Void.class)
            .block();
    }

    public ListLinksResponse getLinks(long id) throws ScrapperApiException {
        return client.get()
            .uri(LINK_BASE_URL)
            .header(CHAT_HEADER, String.valueOf(id))
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::getException)
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public LinkResponse addLink(long id, AddLinkRequest request) throws ScrapperApiException {
        return client.post()
            .uri(LINK_BASE_URL)
            .header(CHAT_HEADER, String.valueOf(id))
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), AddLinkRequest.class)
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::getException)
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public LinkResponse deleteLink(long id, RemoveLinkRequest request) throws ScrapperApiException {
        return client.method(HttpMethod.DELETE)
            .uri(LINK_BASE_URL)
            .header(CHAT_HEADER, String.valueOf(id))
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), RemoveLinkRequest.class)
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::getException)
            .bodyToMono(LinkResponse.class)
            .block();
    }

    private Mono<ScrapperApiException> getException(ClientResponse response) {
        return response
            .bodyToMono(ApiErrorResponse.class)
            .map(ScrapperApiException::new);
    }
}
