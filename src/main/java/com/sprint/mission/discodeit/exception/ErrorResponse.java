package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// 팩토리 메서드 패턴 사용
public record ErrorResponse(
    Instant timestamp,
    String code,
    String message,
    Map<String, Object> details,
    String exceptionType,
    int status
) {


    public static ErrorResponse of(DiscodeitException e) {
        return new ErrorResponse(
            Instant.now(),
            e.getErrorCode().name(),
            e.getMessage(),
            Collections.emptyMap(),
            e.getClass().getSimpleName(),
            e.getErrorCode().getStatus().value()
        );
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(
            Instant.now(),
            errorCode.getCode(),
            errorCode.getMessage(),
            Collections.emptyMap(),
            errorCode.name(),
            errorCode.getStatus().value()
        );
    }

    public static ErrorResponse of(HttpStatus status, Exception e) {
        return new ErrorResponse(
            Instant.now(),
            status.name(),
            e.getMessage(),
            Collections.emptyMap(),
            e.getClass().getSimpleName(),
            status.value()
        );
    }


} 