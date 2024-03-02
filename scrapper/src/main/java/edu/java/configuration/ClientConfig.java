package edu.java.configuration;

import edu.java.client.GithubClient;
import edu.java.client.StackoverflowClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {
    @Value("${app.base-url.github}")
    private String githubUrl;
    @Value("${app.base-url.stackoverflow}")
    private String stackoverflowUrl;

    @Bean
    public GithubClient githubClient() {
        if (githubUrl.isEmpty()) {
            githubUrl = "https://api.github.com/";
        }
        return new GithubClient(githubUrl);
    }

    @Bean
    public StackoverflowClient stackoverflowClient() {
        if (stackoverflowUrl.isEmpty()) {
            stackoverflowUrl = "https://api.stackexchange.com/2.3/";
        }
        return new StackoverflowClient(stackoverflowUrl);
    }
}
