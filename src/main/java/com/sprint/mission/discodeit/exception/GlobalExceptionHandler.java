package com.sprint.mission.discodeit.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleException(DiscodeitException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getTimestamp(),
                e.getErrorCode().getCode(),
                e.getErrorCode().getMessage(),
                e.getDetails(),
                e.getClass().getSimpleName(),
                e.getErrorCode().getHttpStatus().value()
        );
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, Object> details = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> {
                            String msg = fieldError.getDefaultMessage();
                            return msg != null ? msg : "Invalid value";
                        },
                        (msg1, msg2) -> msg1
                ));

        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;

        ErrorResponse errorResponse = new ErrorResponse(
                Instant.now(),
                errorCode.getCode(),
                errorCode.getMessage(),
                details,
                e.getClass().getSimpleName(),
                errorCode.getHttpStatus().value()
        );

        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }
}
