package com.sprint.mission.discodeit.exception.readstatus;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UpdateReadStatusException extends RuntimeException {
    private final HttpStatus status;

    public UpdateReadStatusException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public UpdateReadStatusException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
