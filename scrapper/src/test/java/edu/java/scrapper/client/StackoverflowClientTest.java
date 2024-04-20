package edu.java.scrapper.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.scrapper.client.sources.ResponseException;
import edu.java.scrapper.client.sources.StackoverflowClient;
import edu.java.scrapper.configuration.ObjectMapperConfig;
import edu.java.scrapper.client.sources.dto.SourceUpdate;
import java.util.Map;
import java.util.Optional;
import edu.java.scrapper.configuration.RetryConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.util.retry.Retry;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

class StackoverflowClientTest {
    private WireMockServer server;
    private StackoverflowClient client;
    private final ObjectMapper mapper;
    private final Retry retry;

    StackoverflowClientTest() {
        this.retry = new RetryConfig(false, null, 0, null, null).clientRetry();
        this.mapper = new ObjectMapperConfig().objectMapper();
    }

    @BeforeEach
    void setUp() {
        server = new WireMockServer();
        server.start();
        client = new StackoverflowClient(server.baseUrl(), mapper, retry);
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    public void getUpdate_Ok() throws ResponseException {
        long questionId = 78055556;

        String response1 = """
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
        String uri1 = UriComponentsBuilder.newInstance()
            .path("/questions/{questionId}/answers")
            .uriVariables(Map.of("questionId", questionId))
            .queryParam("site", "stackoverflow")
            .queryParam("sort", "activity")
            .queryParam("order", "desc")
            .queryParam("pagesize", 1)
            .build()
            .toUriString();
        stubFor(get(urlEqualTo(uri1))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(response1)));

        String response2 = """
            {
              "items": [
                {
                  "owner": {
                    "account_id": 6924911,
                    "reputation": 4897,
                    "user_id": 5316255,
                    "user_type": "registered",
                    "accept_rate": 55,
                    "profile_image": "https://i.stack.imgur.com/BYJ8v.jpg?s=256&g=1",
                    "display_name": "Saurabh Tiwari",
                    "link": "https://stackoverflow.com/users/5316255/saurabh-tiwari"
                  },
                  "reply_to_user": {
                    "account_id": 18670408,
                    "reputation": 875,
                    "user_id": 13609359,
                    "user_type": "registered",
                    "profile_image": "https://www.gravatar.com/avatar/eb7f0d944e98518ba472548d46535905?s=256&d=identicon&r=PG&f=y&so-version=2",
                    "display_name": "Slevin",
                    "link": "https://stackoverflow.com/users/13609359/slevin"
                  },
                  "edited": false,
                  "score": 0,
                  "creation_date": 1708874491,
                  "post_id": 78055556,
                  "comment_id": 137608086,
                  "content_license": "CC BY-SA 4.0"
                }
              ],
              "has_more": true,
              "quota_max": 300,
              "quota_remaining": 272
            }
            """;
        String uri2 = UriComponentsBuilder.newInstance()
            .path("/questions/{questionId}/answers")
            .uriVariables(Map.of("questionId", questionId))
            .queryParam("site", "stackoverflow")
            .queryParam("pagesize", 1)
            .build()
            .toUriString();
        stubFor(get(urlEqualTo(uri2))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(response2)));

        Optional<SourceUpdate> expected = Optional.ofNullable(parse(response1));
        Optional<SourceUpdate> actual = client.getUpdate(questionId);
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

    private SourceUpdate parse(String json) {
        try {
            JsonNode rootNode = mapper.readTree(json);
            JsonNode update = rootNode.get("items").get(0);
            return mapper.treeToValue(update, SourceUpdate.class);
        } catch (Exception e) {
            return null;
        }
    }
}
