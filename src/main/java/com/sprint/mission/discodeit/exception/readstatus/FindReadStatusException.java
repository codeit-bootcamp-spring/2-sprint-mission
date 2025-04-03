package com.sprint.mission.discodeit.exception.readstatus;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FindReadStatusException extends RuntimeException {
    private final HttpStatus status;

    public FindReadStatusException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public FindReadStatusException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
