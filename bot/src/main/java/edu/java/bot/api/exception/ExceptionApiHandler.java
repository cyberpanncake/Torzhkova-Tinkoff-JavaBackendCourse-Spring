package edu.java.bot.api.exception;

import edu.java.bot.api.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> anyException(Exception exception) {
        ApiErrorResponse error = new ApiErrorResponse(
            "",
            "",
            exception.getClass().getName(),
            exception.getMessage(),
            new String[] {""});
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error);
    }
}
