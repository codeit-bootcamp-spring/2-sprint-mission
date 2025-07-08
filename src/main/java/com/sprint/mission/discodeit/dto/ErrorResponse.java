package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public record ErrorResponse(
    Instant timestamp,
    String code, // 발생한 클래스 이름
    String message,
    Map<String, Object> details,
    String exceptionType, // 예외발생 클래스 이름
    int status // HTTP 상태코드
) {

    public static ErrorResponse of(
        Exception ex,
        ErrorCode errorCode,
        HttpStatus status,
        Map<String, Object> details
    ) {
        return new ErrorResponse(
            Instant.now(),
            errorCode.name(),
            errorCode.getMessage(),
            details,
            ex.getClass().getName(),
            status.value()
        );
    }

    public static ErrorResponse of(
        Exception ex,
        ErrorCode errorCode,
        HttpStatus status
    ) {
        return new ErrorResponse(
            Instant.now(),
            errorCode.name(),
            errorCode.getMessage(),
            Map.of(),
            ex.getClass().getName(),
            status.value()
        );
    }
}