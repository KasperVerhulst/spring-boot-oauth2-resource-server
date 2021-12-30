package org.rockkit.poc.resourceserver.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse  {

    @JsonFormat(pattern="dd-MMM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;

    private String status;
    private String message;
    private String error;

    public ErrorResponse () {};

    /**
     * Constructor for custom ErrorResponse
     * @param time
     * @param status
     * @param message
     * @param error
     */
    public ErrorResponse(LocalDateTime time, String status, String message, String error) {
        this.timestamp = time;
        this.status = status;
        this.message = message;
        this.error = error;
    }

    /**
     * Constructor for custom ErrorResponse where the timestamp is set to the current time
     * @param status
     * @param message
     * @param error
     */
    public ErrorResponse(String status, String message, String error) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.error = error;
    }

}
