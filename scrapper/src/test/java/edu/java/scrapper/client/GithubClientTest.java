package edu.java.scrapper.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.scrapper.client.sources.GithubClient;
import edu.java.scrapper.client.sources.ResponseException;
import edu.java.scrapper.client.sources.dto.SourceUpdate;
import edu.java.scrapper.configuration.ObjectMapperConfig;
import edu.java.scrapper.client.sources.dto.github.GithubEvent;
import java.util.List;
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

class GithubClientTest {
    private WireMockServer server;
    private GithubClient client;
    private final ObjectMapper mapper;
    private final Retry retry;

    GithubClientTest() {
        this.retry = new RetryConfig(false, null, 0, null, null).clientRetry();
        this.mapper = new ObjectMapperConfig().objectMapper();
    }

    @BeforeEach
    void setUp() {
        server = new WireMockServer();
        server.start();
        client = new GithubClient(server.baseUrl(), mapper, retry);
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    public void getUpdate_Ok() throws ResponseException {
        String author = "cyberpanncake";
        String repo = "Torzhkova-Tinkoff-JavaBackendCourse";

        String response1 = """
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
        String uri1 = UriComponentsBuilder.newInstance()
            .path("/repos/{author}/{repo}/events")
            .uriVariables(Map.of("author", author, "repo", repo))
            .queryParam("per_page", 1)
            .build()
            .toUriString();
        stubFor(get(urlEqualTo(uri1))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(response1)));

        String response2 = """
            [
               {
                 "sha": "295415122835f1731fd9c5f7cd44e690259f14fa",
                 "node_id": "C_kwDOKglvf9oAKDI5NTQxNTEyMjgzNWYxNzMxZmQ5YzVmN2NkNDRlNjkwMjU5ZjE0ZmE",
                 "commit": {
                   "author": {
                     "name": "cyberpanncake",
                     "email": "97220506+cyberpanncake@users.noreply.github.com",
                     "date": "2024-01-08T18:24:31Z"
                   },
                   "committer": {
                     "name": "GitHub",
                     "email": "noreply@github.com",
                     "date": "2024-01-08T18:24:31Z"
                   },
                   "message": "Merge pull request #8 from cyberpanncake/project3\\n\\nfeat: project3",
                   "tree": {
                     "sha": "ac480da95ef625691b0544f5d0a406ff9640b1e5",
                     "url": "https://api.github.com/repos/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse/git/trees/ac480da95ef625691b0544f5d0a406ff9640b1e5"
                   },
                   "url": "https://api.github.com/repos/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse/git/commits/295415122835f1731fd9c5f7cd44e690259f14fa",
                   "comment_count": 0,
                   "verification": {
                     "verified": true,
                     "reason": "valid",
                     "signature": "-----BEGIN PGP SIGNATURE-----\\n\\nwsBcBAABCAAQBQJlnD3fCRBK7hj4Ov3rIwAAqtwIAEzF2OkHo2tTssFfCXskMz8V\\nK6ZcwtTxdduYF8VgbH1SJwdJhtkDHptDRuL7WEhpcINReUuLQq3Cn7stnGgFJjFp\\n+2RVKmTasPCg/Thg0+EC0GJV+mwZbkLkPQsB0pnWa1iieDBa17amOs1KJfTYg1nz\\nfjuuZwplpL4t+HAMNm9j/dppPDSMcjzvauYNe8h0ydLZ0DP+akMxb5d4tsGnpptw\\nkj0BV2Hxj9cjwIe16dpFPhoH/d+JOe7AJ+0pDGky9eXcrrquMOeHA0ktsjIOxxnB\\n4bxxF5aCEYRrTBx84VtRn6CQ1LiOIS7H3YUIgXh7Nd8IQiCn7TXtcWW1yhBCjNE=\\n=oJpW\\n-----END PGP SIGNATURE-----\\n",
                     "payload": "tree ac480da95ef625691b0544f5d0a406ff9640b1e5\\nparent 2329f91e4d384c96307d268b7dc1e5769408d371\\nparent ccdfbd4cd4a9c822ea156f91455e0cf03d5d73d4\\nauthor cyberpanncake <97220506+cyberpanncake@users.noreply.github.com> 1704738271 +0300\\ncommitter GitHub <noreply@github.com> 1704738271 +0300\\n\\nMerge pull request #8 from cyberpanncake/project3\\n\\nfeat: project3"
                   }
                 },
                 "url": "https://api.github.com/repos/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse/commits/295415122835f1731fd9c5f7cd44e690259f14fa",
                 "html_url": "https://github.com/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse/commit/295415122835f1731fd9c5f7cd44e690259f14fa",
                 "comments_url": "https://api.github.com/repos/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse/commits/295415122835f1731fd9c5f7cd44e690259f14fa/comments",
                 "author": {
                   "login": "cyberpanncake",
                   "id": 97220506,
                   "node_id": "U_kgDOBct3mg",
                   "avatar_url": "https://avatars.githubusercontent.com/u/97220506?v=4",
                   "gravatar_id": "",
                   "url": "https://api.github.com/users/cyberpanncake",
                   "html_url": "https://github.com/cyberpanncake",
                   "followers_url": "https://api.github.com/users/cyberpanncake/followers",
                   "following_url": "https://api.github.com/users/cyberpanncake/following{/other_user}",
                   "gists_url": "https://api.github.com/users/cyberpanncake/gists{/gist_id}",
                   "starred_url": "https://api.github.com/users/cyberpanncake/starred{/owner}{/repo}",
                   "subscriptions_url": "https://api.github.com/users/cyberpanncake/subscriptions",
                   "organizations_url": "https://api.github.com/users/cyberpanncake/orgs",
                   "repos_url": "https://api.github.com/users/cyberpanncake/repos",
                   "events_url": "https://api.github.com/users/cyberpanncake/events{/privacy}",
                   "received_events_url": "https://api.github.com/users/cyberpanncake/received_events",
                   "type": "User",
                   "site_admin": false
                 },
                 "committer": {
                   "login": "web-flow",
                   "id": 19864447,
                   "node_id": "MDQ6VXNlcjE5ODY0NDQ3",
                   "avatar_url": "https://avatars.githubusercontent.com/u/19864447?v=4",
                   "gravatar_id": "",
                   "url": "https://api.github.com/users/web-flow",
                   "html_url": "https://github.com/web-flow",
                   "followers_url": "https://api.github.com/users/web-flow/followers",
                   "following_url": "https://api.github.com/users/web-flow/following{/other_user}",
                   "gists_url": "https://api.github.com/users/web-flow/gists{/gist_id}",
                   "starred_url": "https://api.github.com/users/web-flow/starred{/owner}{/repo}",
                   "subscriptions_url": "https://api.github.com/users/web-flow/subscriptions",
                   "organizations_url": "https://api.github.com/users/web-flow/orgs",
                   "repos_url": "https://api.github.com/users/web-flow/repos",
                   "events_url": "https://api.github.com/users/web-flow/events{/privacy}",
                   "received_events_url": "https://api.github.com/users/web-flow/received_events",
                   "type": "User",
                   "site_admin": false
                 },
                 "parents": [
                   {
                     "sha": "2329f91e4d384c96307d268b7dc1e5769408d371",
                     "url": "https://api.github.com/repos/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse/commits/2329f91e4d384c96307d268b7dc1e5769408d371",
                     "html_url": "https://github.com/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse/commit/2329f91e4d384c96307d268b7dc1e5769408d371"
                   },
                   {
                     "sha": "ccdfbd4cd4a9c822ea156f91455e0cf03d5d73d4",
                     "url": "https://api.github.com/repos/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse/commits/ccdfbd4cd4a9c822ea156f91455e0cf03d5d73d4",
                     "html_url": "https://github.com/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse/commit/ccdfbd4cd4a9c822ea156f91455e0cf03d5d73d4"
                   }
                 ]
               }
             ]
            """;
        String uri2 = UriComponentsBuilder.newInstance()
            .path("/repos/{author}/{repo}/commits")
            .uriVariables(Map.of("author", author, "repo", repo))
            .queryParam("per_page", 1)
            .build()
            .toUriString();
        stubFor(get(urlEqualTo(uri2))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(response2)));

        Optional<GithubEvent> expected = Optional.ofNullable(parse(response1));
        Optional<SourceUpdate> actual = client.getUpdate(author, repo);
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

    private GithubEvent parse(String json) {
        try {
            List<GithubEvent> responses = mapper.readValue(json, new TypeReference<>() {
            });
            return responses.get(0);
        } catch (Exception e) {
            return null;
        }
    }
}
