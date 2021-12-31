package org.rockkit.poc.resourceserver.exception;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException() {
        super();
    }

    public BookAlreadyExistsException(String message) {
        super(message);
    }
}
