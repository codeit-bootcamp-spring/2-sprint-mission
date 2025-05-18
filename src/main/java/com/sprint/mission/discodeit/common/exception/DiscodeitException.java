package com.sprint.mission.discodeit.common.exception;

import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
public class DiscodeitException extends RuntimeException {

    private final Instant timestamp;

    private final ErrorCode errorCode;

    private final Map<String, Object> details;

    public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
        this.details = details;
    }

}
