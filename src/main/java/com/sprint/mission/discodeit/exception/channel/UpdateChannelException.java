package com.sprint.mission.discodeit.exception.channel;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UpdateChannelException extends RuntimeException {
    private final HttpStatus status;

    public UpdateChannelException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public UpdateChannelException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
