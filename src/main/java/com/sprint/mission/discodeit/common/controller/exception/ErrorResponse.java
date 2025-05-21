package com.sprint.mission.discodeit.common.controller.exception;

import com.sprint.mission.discodeit.common.exception.DiscodeitException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ErrorResponse(
        Instant timestamp,
        String code,
        String message,
        Map<String, Object> details,
        String exceptionType,
        int status
) {

    public static ErrorResponse ofCustomException(String error, DiscodeitException discodeitException, int status) {
        return new ErrorResponse(
                discodeitException.getTimestamp(),
                error,
                discodeitException.getErrorCode().getMessage(),
                discodeitException.getDetails(),
                discodeitException.getClass().getTypeName(),
                status
        );
    }

    public static ErrorResponse of(String error, Exception ex, int status) {
        return new ErrorResponse(
                Instant.now(),
                error,
                ex.getMessage(),
                null,
                ex.getClass().getTypeName(),
                status
        );
    }

    public static List<ErrorResponse> ofCustomException(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(fieldError -> new ErrorResponse(
                        Instant.now(),
                        HttpStatus.BAD_REQUEST.toString(),
                        fieldError.getDefaultMessage(),
                        Map.of("field", fieldError.getField()),
                        fieldError.getClass().getTypeName(),
                        HttpStatus.BAD_REQUEST.value())
                )
                .toList();
    }
}
