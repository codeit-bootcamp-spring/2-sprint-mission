package com.sprint.mission.discodeit.exception.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UpdateUserException extends RuntimeException {
    private final HttpStatus status;

    public UpdateUserException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public UpdateUserException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
