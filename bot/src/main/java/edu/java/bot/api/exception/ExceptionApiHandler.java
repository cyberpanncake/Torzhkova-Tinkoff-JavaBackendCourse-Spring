package edu.java.bot.api.exception;

import edu.java.dto.api.bot.ApiErrorResponse;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice public class ExceptionApiHandler {

    @ExceptionHandler(ChatsNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> chatNotExistException(ChatsNotFoundException exception) {
        ApiErrorResponse error = new ApiErrorResponse(
            "Чаты не существуют: %s".formatted(String.join(", ", exception.getTgIds().stream()
                .map(String::valueOf).toArray(String[]::new))),
            HttpStatus.NOT_FOUND.toString(),
            exception.getClass().getName(),
            exception.getMessage(),
            Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(Exception.class) public ResponseEntity<ApiErrorResponse> anyException(Exception exception) {
        ApiErrorResponse error = new ApiErrorResponse(
            "Произошла непредвиденная ошибка на стороне сервера",
            HttpStatus.INTERNAL_SERVER_ERROR.toString(),
            exception.getClass().getName(),
            exception.getMessage(),
            Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new)
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
