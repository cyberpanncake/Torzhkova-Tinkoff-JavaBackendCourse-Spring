package edu.java.bot.api.exception;

import edu.java.api_dto.bot.ApiErrorResponse;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice public class ExceptionApiHandler {

    @ExceptionHandler(ChatNotExistException.class)
    public ResponseEntity<ApiErrorResponse> chatNotExistException(ChatNotExistException exception) {
        ApiErrorResponse error = new ApiErrorResponse(
            "Чат с id %d не существует".formatted(exception.getChatId()),
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
