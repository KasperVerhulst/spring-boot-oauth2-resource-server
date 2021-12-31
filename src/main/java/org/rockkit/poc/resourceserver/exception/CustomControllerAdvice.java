package org.rockkit.poc.resourceserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookNotFoundException(BookNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), e.getMessage(), e.toString()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleBookAlreadyExistsException(BookAlreadyExistsException e) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.CONFLICT.toString(), e.getMessage(), e.toString()),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage(), e.toString()),HttpStatus.BAD_REQUEST);
    }
}
