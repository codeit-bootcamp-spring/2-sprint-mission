package com.sprint.mission.discodeit.exception.message;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DeleteMessageException extends RuntimeException {
    private final HttpStatus status;

    public DeleteMessageException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public DeleteMessageException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
