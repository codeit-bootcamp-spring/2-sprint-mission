package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.*;

import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

    final Instant timestamp;
    final ErrorCode errorCode;
    final Map<String, Object> details;

    public DiscodeitException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
        this.details = Collections.emptyMap();
    }

    public DiscodeitException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
        this.details = Collections.emptyMap();
    }

}
