package edu.java.scrapper.api.exception.link;

import edu.java.api_dto.scrapper.ApiErrorResponse;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LinkExceptionApiHandler {

    @ExceptionHandler(LinkAdditionException.class)
    public ResponseEntity<ApiErrorResponse> linkAdditionException(LinkAdditionException exception) {
        ApiErrorResponse error = new ApiErrorResponse(
            "Ссылка уже была добавлена",
            HttpStatus.BAD_REQUEST.toString(),
            exception.getClass().getName(),
            exception.getMessage(),
            Arrays.stream(exception.getStackTrace())
                .map(StackTraceElement::toString)
                .toArray(String[]::new)
        );
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }

    @ExceptionHandler(LinkNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> linkNotFoundException(LinkNotFoundException exception) {
        ApiErrorResponse error = new ApiErrorResponse(
            "Ссылка не найдена",
            HttpStatus.NOT_FOUND.toString(),
            exception.getClass().getName(),
            exception.getMessage(),
            Arrays.stream(exception.getStackTrace())
                .map(StackTraceElement::toString)
                .toArray(String[]::new)
        );
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> anyException(Exception exception) {
        ApiErrorResponse error = new ApiErrorResponse(
            "Произошла непредвиденная ошибка на стороне сервера",
            HttpStatus.INTERNAL_SERVER_ERROR.toString(),
            exception.getClass().getName(),
            exception.getMessage(),
            Arrays.stream(exception.getStackTrace())
                .map(StackTraceElement::toString)
                .toArray(String[]::new)
        );
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(error);
    }
}
