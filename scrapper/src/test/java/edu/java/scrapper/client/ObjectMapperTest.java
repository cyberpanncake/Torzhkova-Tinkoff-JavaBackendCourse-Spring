package edu.java.scrapper.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.scrapper.configuration.ObjectMapperConfig;
import edu.java.scrapper.client.sources.dto.github.GithubEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;

public class ObjectMapperTest {

    private final ObjectMapper mapper;

    public ObjectMapperTest() {
        this.mapper = new ObjectMapperConfig().objectMapper();
    }

    @Test
    void objectMapperTest() throws JsonProcessingException {
        GithubEvent object = new GithubEvent(0L, "example",
            new GithubEvent.Actor("cyberpanncake"),
            new GithubEvent.Repository("Torzhkova-Tinkoff-JavaBackendCourse-Spring"),
            OffsetDateTime.now());
        String json = mapper.writeValueAsString(object);
        Assertions.assertTrue(json.contains("created_at"));
    }
}
