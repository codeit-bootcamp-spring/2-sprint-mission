package com.sprint.mission.discodeit.controller.advice;

import com.sprint.mission.discodeit.constant.ErrorCode;
import com.sprint.mission.discodeit.dto.api.ErrorResponse;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.mapper.ErrorResponseMapper;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
        HttpStatus status = mapToHttpStatus(e.getErrorCode());
        return ResponseEntity
            .status(status)
            .body(ErrorResponseMapper.from(e, status));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnhandledExceptions(Exception e) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(
                Instant.now(),
                "UNHANDLED",
                e.getMessage(),
                Map.of(),
                e.getClass().getSimpleName(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
            ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        Map<String, Object> fieldErrorDetails = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                FieldError::getDefaultMessage,
                (existing, replacement) -> existing
            ));

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(
                Instant.now(),
                ErrorCode.INVALID_ARGUMENT.name(),
                ErrorCode.INVALID_ARGUMENT.getMessage(),
                fieldErrorDetails,
                e.getClass().getSimpleName(),
                HttpStatus.BAD_REQUEST.value()
            ));
    }

    private HttpStatus mapToHttpStatus(ErrorCode errorCode) {
        return switch (errorCode) {
            case USER_NOT_FOUND, CHANNEL_NOT_FOUND, MESSAGE_NOT_FOUND, BINARY_CONTENT_NOT_FOUND,
                 USER_STATUS_NOT_FOUND, READ_STATUS_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case DUPLICATE_USER, DUPLICATE_READ_STATUS -> HttpStatus.CONFLICT;
            case PRIVATE_CHANNEL_UPDATE -> HttpStatus.BAD_REQUEST;
            case WRONG_PASSWORD -> HttpStatus.UNAUTHORIZED;

            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

}