package edu.java.scrapper.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.scrapper.client.sources.ResponseException;
import edu.java.scrapper.client.sources.StackoverflowClient;
import edu.java.scrapper.configuration.ObjectMapperConfig;
import edu.java.scrapper.dto.StackoverflowResponse;
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

class StackoverflowClientTest {
    private WireMockServer server;
    private StackoverflowClient client;
    private final ObjectMapper mapper;

    StackoverflowClientTest() {
        this.mapper = new ObjectMapperConfig().objectMapper();
    }

    @BeforeEach
    void setUp() {
        server = new WireMockServer();
        server.start();
        client = new StackoverflowClient(server.baseUrl(), mapper);
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    public void getUpdate_Ok() throws ResponseException {
        long questionId = 78055556;
        String response = """
            {
              "items": [
                {
                  "owner": {
                    "account_id": 14141257,
                    "reputation": 141,
                    "user_id": 10215969,
                    "user_type": "registered",
                    "profile_image": "https://www.gravatar.com/avatar/46ff882fa33d9657e2e7f888c87d67d1?s=256&d=identicon&r=PG&f=y&so-version=2",
                    "display_name": "ankush__",
                    "link": "https://stackoverflow.com/users/10215969/ankush"
                  },
                  "is_accepted": false,
                  "score": 0,
                  "last_activity_date": 1708877709,
                  "last_edit_date": 1708877709,
                  "creation_date": 1708876876,
                  "answer_id": 78056658,
                  "question_id": 78055556,
                  "content_license": "CC BY-SA 4.0"
                }
              ],
              "has_more": true,
              "quota_max": 300,
              "quota_remaining": 294
            }
            """;
        String uri = UriComponentsBuilder.newInstance()
            .path("/questions/{questionId}/answers")
            .uriVariables(Map.of("questionId", questionId))
            .queryParam("site", "stackoverflow")
            .queryParam("sort", "activity")
            .queryParam("order", "desc")
            .queryParam("pagesize", 1)
            .build()
            .toUriString();
        stubFor(get(urlEqualTo(uri))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(response)));
        Optional<StackoverflowResponse> expected = Optional.ofNullable(parse(response));
        Optional<StackoverflowResponse> actual = client.getUpdate(questionId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getUpdate_NotFound() {
        long questionId = 0;
        String response = """
            {
              "items": [],
              "has_more": false,
              "quota_max": 300,
              "quota_remaining": 293
            }
            """;
        String uri = UriComponentsBuilder.newInstance()
            .path("/questions/{questionId}/answers")
            .uriVariables(Map.of("questionId", questionId))
            .queryParam("site", "stackoverflow")
            .queryParam("sort", "activity")
            .queryParam("order", "desc")
            .queryParam("pagesize", 1)
            .build()
            .toUriString();
        stubFor(get(urlEqualTo(uri))
            .willReturn(aResponse().withStatus(400)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(response)));
        Assertions.assertThrows(ResponseException.class, () -> client.getUpdate(questionId));
    }

    private StackoverflowResponse parse(String json) {
        try {
            JsonNode rootNode = mapper.readTree(json);
            JsonNode update = rootNode.get("items").get(0);
            return mapper.treeToValue(update, StackoverflowResponse.class);
        } catch (Exception e) {
            return null;
        }
    }
}
