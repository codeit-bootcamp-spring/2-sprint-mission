package com.sprint.mission.discodeit.exception.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSuchIdException extends RuntimeException {
    private final HttpStatus status;

    public NoSuchIdException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public NoSuchIdException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
