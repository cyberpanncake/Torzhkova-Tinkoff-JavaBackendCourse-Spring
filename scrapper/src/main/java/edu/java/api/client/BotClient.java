package edu.java.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.client.AbstractClient;
import lombok.NonNull;

public class BotClient extends AbstractClient {

    public BotClient(@NonNull String baseUrl, ObjectMapper mapper) {
        super(baseUrl, mapper);
    }
}
