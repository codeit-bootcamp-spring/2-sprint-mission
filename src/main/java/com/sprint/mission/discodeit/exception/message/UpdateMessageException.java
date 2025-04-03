package com.sprint.mission.discodeit.exception.message;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UpdateMessageException extends RuntimeException {
    private final HttpStatus status;

    public UpdateMessageException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
