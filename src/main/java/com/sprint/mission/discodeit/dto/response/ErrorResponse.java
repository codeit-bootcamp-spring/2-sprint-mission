package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
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
}
