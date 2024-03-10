package edu.java.bot.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.api.client.ScrapperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {
    @Value("${resources.base-url.scrapper}")
    private String scrapperUrl;
    private final ObjectMapper objectMapper;

    @Bean
    public ScrapperClient scrapperClient() {
        return new ScrapperClient(
            scrapperUrl,
            objectMapper
        );
    }
}
