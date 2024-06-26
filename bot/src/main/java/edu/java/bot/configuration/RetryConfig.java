package edu.java.bot.configuration;

import edu.java.dto.retry.BackoffStrategy;
import edu.java.dto.retry.RetryBuilder;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import reactor.util.retry.Retry;

@ConfigurationProperties(prefix = "retry", ignoreUnknownFields = false)
public record RetryConfig(Boolean enable, String[] codes, int maxAttempts, BackoffStrategy strategy,
                          Duration interval) {

    @Bean
    public Retry clientRetry() {
        if (enable != null && enable) {
            return RetryBuilder.build(codes, maxAttempts, strategy, interval);
        }
        return Retry.max(0).filter(thr -> false);
    }
}
