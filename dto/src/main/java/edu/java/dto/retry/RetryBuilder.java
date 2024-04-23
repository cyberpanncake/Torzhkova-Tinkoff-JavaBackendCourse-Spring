package edu.java.dto.retry;

import edu.java.dto.api.exception.ApiException;
import java.time.Duration;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

public class RetryBuilder {
    private static final String RETRY_EXHAUSTED_CODE = String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value());
    private static final String RETRY_EXHAUSTED_MESSAGE = "Все попытки доступа к сервису были исчерпаны";

    private RetryBuilder() {
    }

    public static Retry build(String[] codes, int maxAttempts, BackoffStrategy strategy, Duration interval) {
        return switch (strategy) {
            case FIXED -> getRetryWithFixedBackoff(codes, maxAttempts, interval);
            case LINEAR -> getRetryWithLinearBackoff(codes, maxAttempts, interval);
            case EXPONENTIAL -> getRetryWithExponentialBackoff(codes, maxAttempts, interval);
        };
    }

    private static Predicate<? super Throwable> getFilter(String[] codes) {
        return e -> e instanceof ApiException && codes != null
            && List.of(codes).contains(((ApiException) e).getHttpCode());
    }

    private static BiFunction<RetryBackoffSpec, Retry.RetrySignal, Throwable> getRetryExhaustedError() {
        return (backoffSpec, signal) -> {
            throw new ApiException(RETRY_EXHAUSTED_CODE, RETRY_EXHAUSTED_MESSAGE);
        };
    }

    private static Retry getRetryWithFixedBackoff(String[] codes, int maxAttempts, Duration interval) {
        return Retry.fixedDelay(maxAttempts, interval)
            .filter(getFilter(codes))
            .onRetryExhaustedThrow(getRetryExhaustedError());
    }

    private static Retry getRetryWithLinearBackoff(String[] codes, int maxAttempts, Duration interval) {
        var filter = getFilter(codes);
        return Retry.from(signal -> Flux.deferContextual(
                cv -> signal.contextWrite(cv)
                    .concatMap(retryWhenState -> {
                        Retry.RetrySignal copy = retryWhenState.copy();
                        Throwable currentFailure = copy.failure();
                        long iteration = copy.totalRetries();
                        if (currentFailure == null) {
                            return Mono.error(
                                new IllegalStateException("Retry.RetrySignal#failure() not expected to be null")
                            );
                        }
                        if (!filter.test(currentFailure)) {
                            return Mono.error(currentFailure);
                        }
                        if (iteration >= maxAttempts) {
                            return Mono.error(new ApiException(RETRY_EXHAUSTED_CODE, RETRY_EXHAUSTED_MESSAGE));
                        }
                        Duration nextBackoff = interval.multipliedBy(iteration);
                        return Mono.delay(nextBackoff, Schedulers.parallel());
                    })
            )
        );
    }

    private static Retry getRetryWithExponentialBackoff(String[] codes, int maxAttempts, Duration interval) {
        return Retry.backoff(maxAttempts, interval)
            .filter(getFilter(codes))
            .onRetryExhaustedThrow(getRetryExhaustedError());
    }
}
