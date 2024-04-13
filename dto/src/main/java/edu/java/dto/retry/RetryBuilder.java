package edu.java.dto.retry;

import edu.java.dto.api.exception.ApiException;
import java.time.Duration;
import org.springframework.http.HttpStatus;
import reactor.util.retry.Retry;

public class RetryBuilder {

    private RetryBuilder() {
    }

    public static Retry build(String[] codes, int maxAttempts, BackoffStrategy strategy, Duration interval) {
        return Retry.max(maxAttempts).onRetryExhaustedThrow((backoffSpec, signal) -> {
            throw new ApiException(
                String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()),
                "Все попытки доступа к сервису были исчерпаны"
            );
        });
    }
}
