package edu.java.scrapper.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.client.sources.GithubClient;
import edu.java.scrapper.client.sources.StackoverflowClient;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableCaching
public class ClientConfig {
    private static final String DEFAULT_GITHUB_URL = "https://api.github.com/";
    private static final String DEFAULT_STACKOVERFLOW_URL = "https://api.stackexchange.com/2.3/";
    @Value("${resources.base-url.github}")
    private String githubUrl;
    @Value("${resources.base-url.stackoverflow}")
    private String stackoverflowUrl;
    @Value("${resources.base-url.bot}")
    private String botUrl;
    private final ObjectMapper objectMapper;

    @Bean
    public GithubClient githubClient() {
        return new GithubClient(
            Strings.isNotEmpty(githubUrl) ? githubUrl : DEFAULT_GITHUB_URL,
            objectMapper
        );
    }

    @Bean
    public StackoverflowClient stackoverflowClient() {
        return new StackoverflowClient(
            Strings.isNotEmpty(stackoverflowUrl) ? stackoverflowUrl : DEFAULT_STACKOVERFLOW_URL,
            objectMapper
        );
    }

    @Bean
    public BotClient botClient() {
        return new BotClient(botUrl, objectMapper);
    }
}
