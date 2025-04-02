package com.sprint.mission.discodeit.exception.userstatus;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UpdateUserStatusException extends RuntimeException {
    private final HttpStatus status;

    public UpdateUserStatusException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public UpdateUserStatusException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
