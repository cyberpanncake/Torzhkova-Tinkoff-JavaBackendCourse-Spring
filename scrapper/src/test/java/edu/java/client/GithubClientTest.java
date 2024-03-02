package edu.java.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.dto.GithubResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

class GithubClientTest {
    private WireMockServer server;
    private GithubClient client;

    @BeforeEach
    void setUp() {
        server = new WireMockServer();
        server.start();
        client = new GithubClient(server.baseUrl());
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    public void getUpdate_Ok() throws ResponseException {
        String author = "cyberpanncake";
        String repo = "Torzhkova-Tinkoff-JavaBackendCourse";
        String response = """
            [
              {
                "id": "35443777108",
                "type": "WatchEvent",
                "actor": {
                  "id": 130314986,
                  "login": "saundler",
                  "display_login": "saundler",
                  "gravatar_id": "",
                  "url": "https://api.github.com/users/saundler",
                  "avatar_url": "https://avatars.githubusercontent.com/u/130314986?"
                },
                "repo": {
                  "id": 705261439,
                  "name": "cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse",
                  "url": "https://api.github.com/repos/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse"
                },
                "payload": {
                  "action": "started"
                },
                "public": true,
                "created_at": "2024-02-06T12:55:12Z"
              }
            ]
            """;
        String uri = UriComponentsBuilder.newInstance()
            .path("/repos/{author}/{repo}/events")
            .uriVariables(Map.of("author", author, "repo", repo))
            .queryParam("per_page", 1)
            .build()
            .toUriString();
        stubFor(get(urlEqualTo(uri))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(response)));
        Optional<GithubResponse> expected = Optional.ofNullable(parse(response));
        Optional<GithubResponse> actual = client.getUpdate(author, repo);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getUpdate_NotFound() {
        String author = "";
        String repo = "";
        String response = """
            {
                "message": "Not Found",
                "documentation_url": "https://docs.github.com/rest"
              }
            """;
        String uri = UriComponentsBuilder.newInstance()
            .path("/repos/{author}/{repo}/events")
            .uriVariables(Map.of("author", author, "repo", repo))
            .queryParam("per_page", 1)
            .build()
            .toUriString();
        stubFor(get(urlEqualTo(uri))
            .willReturn(aResponse().withStatus(400)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(response)));
        Assertions.assertThrows(ResponseException.class, () -> client.getUpdate(author, repo));
    }

    private GithubResponse parse(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            List<GithubResponse> responses = objectMapper.readValue(json, new TypeReference<>() {
            });
            return responses.get(0);
        } catch (Exception e) {
            return null;
        }
    }
}