package edu.java.bot.api.exception;

import edu.java.bot.api.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(ChatNotExistException.class)
    public ResponseEntity<ApiErrorResponse> chatNotExistException(ChatNotExistException exception) {
        ApiErrorResponse error = new ApiErrorResponse(
            "Чат с id %d не существует".formatted(exception.getChatId()),
            HttpStatus.NOT_FOUND.toString(),
            exception.getClass().getName(),
            exception.getMessage(),
            exception.getStackTrace()
        );
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> anyException(Exception exception) {
        ApiErrorResponse error = new ApiErrorResponse(
            "Неверные параметры запроса",
            HttpStatus.BAD_REQUEST.toString(),
            exception.getClass().getName(),
            exception.getMessage(),
            exception.getStackTrace()
        );
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }
}
