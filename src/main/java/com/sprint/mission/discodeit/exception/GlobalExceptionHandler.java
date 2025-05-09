package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.error.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
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
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
    ErrorCode errorCode = e.getErrorCode();
    ErrorResponse errorResponse = new ErrorResponse(
            e.getTimestamp(),
            errorCode.getHttpStatus().value(),
            errorCode.name(),
            errorCode.getMessage(),
            e.getDetails(),
            e.getClass().getSimpleName()
    );
    return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    ErrorResponse errorResponse = new ErrorResponse(
            Instant.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            ErrorCode.INTERNAL_SERVER_ERROR.name(),
            ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
            Map.of("exception", e.getClass().getSimpleName()),
            e.getClass().getSimpleName()
    );
    return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
    Map<String, Object> errors = e.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(
                    FieldError::getField,
                    DefaultMessageSourceResolvable::getDefaultMessage,
                    (msg1, msg2) -> msg1 + ", " + msg2 // 필드 중복 시 메시지 합침
            ));

    ErrorResponse errorResponse = new ErrorResponse(
            Instant.now(),
            ErrorCode.VALIDATION_ERROR.getHttpStatus().value(),
            ErrorCode.VALIDATION_ERROR.name(),
            "Validation failed",
            errors,
            e.getClass().getSimpleName()
    );
    return ResponseEntity
            .status(ErrorCode.VALIDATION_ERROR.getHttpStatus())
            .body(errorResponse);
  }
}