package com.sprint.mission.discodeit.exception.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ReadAllUserException extends RuntimeException {
    private final HttpStatus status;

    public ReadAllUserException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public ReadAllUserException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
