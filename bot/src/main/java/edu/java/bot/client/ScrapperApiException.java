package edu.java.bot.client;

import edu.java.dto.api.scrapper.ApiErrorResponse;
import lombok.Getter;

@Getter
public class ScrapperApiException extends Exception {
    private final ApiErrorResponse error;

    public ScrapperApiException(ApiErrorResponse error) {
        super("%s: %s. %s - %s\n%s".formatted(error.code(), error.description(),
            error.exceptionName(), error.exceptionMessage(), String.join("\n", error.stacktrace())));
        this.error = error;
    }
}
