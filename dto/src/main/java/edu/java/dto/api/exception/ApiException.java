package edu.java.dto.api.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    protected final String httpCode;

    public ApiException(String httpCode, String message) {
        super(message);
        this.httpCode = httpCode;
    }
}
