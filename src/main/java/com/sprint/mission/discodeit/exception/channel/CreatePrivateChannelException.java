package com.sprint.mission.discodeit.exception.channel;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CreatePrivateChannelException extends RuntimeException {
    private final HttpStatus status;

    public CreatePrivateChannelException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public CreatePrivateChannelException(String message, HttpStatus status, Throwable cause) {
      super(message, cause);
      this.status = status;
    }
}
