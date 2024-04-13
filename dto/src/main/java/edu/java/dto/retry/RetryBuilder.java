package edu.java.dto.retry;

import java.time.Duration;
import reactor.util.retry.Retry;

public class RetryBuilder {

    private RetryBuilder() { }

    public static Retry build(String[] codes, int maxAttempts, BackoffStrategy strategy, Duration interval) {
        return Retry.max(maxAttempts);
    }
}
