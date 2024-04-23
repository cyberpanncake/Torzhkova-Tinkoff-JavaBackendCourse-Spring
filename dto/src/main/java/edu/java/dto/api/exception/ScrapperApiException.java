package edu.java.dto.api.exception;

import edu.java.dto.api.scrapper.ApiErrorResponse;
import lombok.Getter;

@Getter
public class ScrapperApiException extends ApiException {
    private final ApiErrorResponse error;

    public ScrapperApiException(ApiErrorResponse error) {
        super(error.code(), "%s: %s. %s - %s\n%s".formatted(error.code(), error.description(),
            error.exceptionName(), error.exceptionMessage(), String.join("\n", error.stacktrace())));
        this.error = error;
    }
}
