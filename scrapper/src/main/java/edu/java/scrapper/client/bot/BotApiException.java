package edu.java.scrapper.client.bot;

import edu.java.api_dto.bot.ApiErrorResponse;
import lombok.Getter;

@Getter
public class BotApiException extends Exception {
    private final ApiErrorResponse error;

    public BotApiException(ApiErrorResponse error) {
        super("%s: %s. %s - %s\n%s".formatted(error.code(), error.description(),
            error.exceptionName(), error.exceptionMessage(), String.join("\n", error.stacktrace())));
        this.error = error;
    }
}
