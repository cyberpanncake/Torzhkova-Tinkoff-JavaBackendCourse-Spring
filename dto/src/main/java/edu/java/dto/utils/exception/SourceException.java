package edu.java.dto.utils.exception;

public abstract class SourceException extends UrlException {
    public SourceException() {
    }

    public SourceException(String message) {
        super(message);
    }
}
