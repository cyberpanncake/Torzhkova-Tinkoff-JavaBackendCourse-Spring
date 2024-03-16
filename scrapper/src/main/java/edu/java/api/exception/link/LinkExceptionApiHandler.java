package edu.java.api.exception.link;

import edu.java.api.dto.ApiErrorResponse;
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
            exception.getStackTrace()
        );
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }

    @ExceptionHandler(LinkAdditionException.class)
    public ResponseEntity<ApiErrorResponse> linkNotFoundException(LinkAdditionException exception) {
        ApiErrorResponse error = new ApiErrorResponse(
            "Ссылка не найдена",
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
