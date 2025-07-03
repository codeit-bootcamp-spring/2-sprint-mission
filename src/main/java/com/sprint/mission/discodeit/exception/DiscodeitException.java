package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.*;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

    final Instant timestamp;
    final ErrorCode errorCode;

    protected DiscodeitException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
    }

    protected DiscodeitException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
    }

    protected DiscodeitException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
    }

    protected DiscodeitException(ErrorCode errorCode, String customMessage, Throwable cause) {
        super(customMessage, cause);
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
    }
}
