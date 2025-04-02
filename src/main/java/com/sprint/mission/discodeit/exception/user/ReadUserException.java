package com.sprint.mission.discodeit.exception.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ReadUserException extends RuntimeException {
    private final HttpStatus status;

    public ReadUserException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public ReadUserException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
