package edu.java.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.configuration.ObjectMapperConfig;
import edu.java.dto.GithubResponse;
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
        GithubResponse object = new GithubResponse(0L, "example",
            new GithubResponse.Actor("cyberpanncake"),
            new GithubResponse.Repository("Torzhkova-Tinkoff-JavaBackendCourse-Spring"),
            OffsetDateTime.now());
        String json = mapper.writeValueAsString(object);
        Assertions.assertTrue(json.contains("created_at"));
    }
}
