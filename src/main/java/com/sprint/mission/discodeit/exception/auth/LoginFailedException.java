package com.sprint.mission.discodeit.exception.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LoginFailedException extends RuntimeException {
    private final HttpStatus status;

    public LoginFailedException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    public LoginFailedException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
