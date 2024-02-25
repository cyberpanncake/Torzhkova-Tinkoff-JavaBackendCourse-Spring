package edu.java.configuration;

import edu.java.client.GithubClient;
import edu.java.client.StackoverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Bean
    public GithubClient githubClient() {
        return new GithubClient();
    }

    @Bean
    public StackoverflowClient stackoverflowClient() {
        return new StackoverflowClient();
    }
}
