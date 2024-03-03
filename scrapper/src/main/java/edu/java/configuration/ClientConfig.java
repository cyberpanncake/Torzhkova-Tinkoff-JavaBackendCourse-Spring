package edu.java.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.client.GithubClient;
import edu.java.client.StackoverflowClient;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {
    private static final String DEFAULT_GITHUB_URL = "https://api.github.com/";
    private static final String DEFAULT_STACKOVERFLOW_URL = "https://api.stackexchange.com/2.3/";
    @Value("${resources.base-url.github}")
    private String githubUrl;
    @Value("${resources.base-url.stackoverflow}")
    private String stackoverflowUrl;
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
}
