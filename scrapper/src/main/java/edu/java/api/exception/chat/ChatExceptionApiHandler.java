package edu.java.api.exception.chat;

import edu.java.api.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChatExceptionApiHandler {

    @ExceptionHandler(ChatRegistrationException.class)
    public ResponseEntity<ApiErrorResponse> chatRegistrationException(ChatRegistrationException exception) {
        ApiErrorResponse error = new ApiErrorResponse(
            "Чат с уже зарегистрирован",
            HttpStatus.BAD_REQUEST.toString(),
            exception.getClass().getName(),
            exception.getMessage(),
            exception.getStackTrace()
        );
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }

    @ExceptionHandler(ChatRegistrationException.class)
    public ResponseEntity<ApiErrorResponse> chatNotFoundException(ChatRegistrationException exception) {
        ApiErrorResponse error = new ApiErrorResponse(
            "Чат с id не найден",
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
