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

    public record ValidationError(String field, String message) {

    }

    // new 사용 x 호출하여 사용할 수 있음
    //of 여러 매개변수를 받아 적합한 타입의 인스턴스를 반환하는 집계 메소드
    public static ErrorResponse of(DiscodeitException e) {
        Map<String, Object> detailsMap = new HashMap<>();
        if (e.getDetails() != null) {
            detailsMap.put("details", e.getDetails());
        }

        return new ErrorResponse(
            Instant.now(),
            e.getErrorCode().name(),
            e.getMessage(),
            detailsMap,
            e.getClass().getSimpleName(),
            e.getErrorCode().getStatus().value()
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

    // 커스텀 메시지 처리 팩토리 메소드
    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(
            Instant.now(),
            errorCode.name(),
            message,
            Collections.emptyMap(),
            "CustomError",
            errorCode.getStatus().value()
        );
    }

    // 유효성 검증 오류 추가 메소드
    public static ErrorResponse ofValidation(ErrorCode errorCode, String message,
        Map<String, Object> validationErrors) {
        Map<String, Object> detailsMap = new HashMap<>();
        detailsMap.put("validationErrors", validationErrors);

        return new ErrorResponse(
            Instant.now(),
            errorCode.name(),
            message,
            detailsMap,
            "ValidationError",
            errorCode.getStatus().value()
        );
    }
} 