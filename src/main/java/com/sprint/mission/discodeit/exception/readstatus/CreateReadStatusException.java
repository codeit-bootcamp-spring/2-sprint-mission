package com.sprint.mission.discodeit.exception.readstatus;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CreateReadStatusException extends RuntimeException {
    private final HttpStatus status;

    public CreateReadStatusException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public CreateReadStatusException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
