package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        Instant timestamp,
        String code,
        String message,
        Map<String, Object> details,
        String exceptionType,
        int status
) {
    public ErrorResponse(
            DiscodeitException exception
    ) {
        this(
                exception.getTimestamp(),
                exception.getErrorCode().getCode(),
                exception.getMessage(),
                exception.getDetails(),
                exception.getClass().getSimpleName(),
                exception.getErrorCode().getHttpStatus()
        );
    }

    public ErrorResponse(Exception e, ErrorCode errorCode) {
        this(
                Instant.now(),
                errorCode.getCode(),
                errorCode.getMessage(),
                Map.of(),
                e.getClass().getSimpleName(),
                errorCode.getHttpStatus()
        );
    }

    public ErrorResponse(Exception e, ErrorCode errorCode, Map<String, Object> details) {
        this(
                Instant.now(),
                errorCode.getCode(),
                errorCode.getMessage(),
                details,
                e.getClass().getSimpleName(),
                errorCode.getHttpStatus()
        );
    }
}
