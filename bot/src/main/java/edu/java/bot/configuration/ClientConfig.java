package edu.java.bot.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.client.ScrapperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {
    @Value("${resources.base-url.scrapper}")
    private String scrapperUrl;
    private final ObjectMapper objectMapper;
    private final Retry retry;

    @Bean
    public ScrapperClient scrapperClient() {
        return new ScrapperClient(
            scrapperUrl,
            objectMapper,
            retry
        );
    }
}
