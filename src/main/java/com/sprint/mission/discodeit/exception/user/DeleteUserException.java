package com.sprint.mission.discodeit.exception.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DeleteUserException extends RuntimeException {
    private final HttpStatus status;

    public DeleteUserException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public DeleteUserException(String message, HttpStatus status, Throwable cause) {
      super(message, cause);
      this.status = status;
    }
}
