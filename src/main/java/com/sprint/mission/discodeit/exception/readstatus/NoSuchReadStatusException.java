package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSuchReadStatusException extends RuntimeException {
    private final HttpStatus status;

    public NoSuchReadStatusException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public NoSuchReadStatusException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
