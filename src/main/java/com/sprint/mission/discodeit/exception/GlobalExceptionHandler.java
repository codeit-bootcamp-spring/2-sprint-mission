package com.sprint.mission.discodeit.exception;

import io.swagger.v3.oas.annotations.Hidden;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex) {
    Map<String, Object> details = ex.getBindingResult().getFieldErrors().stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            FieldError::getDefaultMessage,
            (msg1, msg2) -> msg1 // 필드 중복 시 첫 번째 메시지 유지
        ));
    HttpStatus status = ErrorCode.BAD_REQUEST.getStatus();
    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        ErrorCode.BAD_REQUEST.name(),
        ex.getMessage(),
        details,
        ex.getClass().getSimpleName(),
        status.value()
    );
    return ResponseEntity.status(status).body(errorResponse);
  }

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException ex) {
    HttpStatus status = ex.getErrorCode().getStatus();
    ErrorResponse errorResponse = new ErrorResponse(
        ex.getTimestamp(),
        ex.getErrorCode().name(),
        ex.getMessage(),
        ex.getDetails(),
        ex.getClass().getSimpleName(),
        status.value()
    );
    return ResponseEntity.status(status).body(errorResponse);
  }

  // 404: Not Found 예외 처리
  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex) {
    HttpStatus status = ErrorCode.NOT_FOUND.getStatus();
    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        ErrorCode.NOT_FOUND.name(),
        ex.getMessage(),
        Collections.emptyMap(),
        ex.getClass().getSimpleName(),
        status.value()
    );
    return ResponseEntity.status(status).body(errorResponse);
  }

  // 400: Bad Request 예외 처리
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
    HttpStatus status = ErrorCode.BAD_REQUEST.getStatus();
    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        ErrorCode.BAD_REQUEST.name(),
        ex.getMessage(),
        Collections.emptyMap(),
        ex.getClass().getSimpleName(),
        status.value()
    );
    return ResponseEntity.status(status).body(errorResponse);
  }

  // 403: FORBIDDEN 예외 처리
  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
    HttpStatus status = ErrorCode.FORBIDDEN.getStatus();
    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        ErrorCode.FORBIDDEN.name(),
        ex.getMessage(),
        Collections.emptyMap(),
        ex.getClass().getSimpleName(),
        status.value()
    );
    return ResponseEntity.status(status).body(errorResponse);
  }

  // 500: 런타임 오류 처리
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
    HttpStatus status = ErrorCode.INTERNAL_SERVER_ERROR.getStatus();
    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        ErrorCode.INTERNAL_SERVER_ERROR.name(),
        ex.getMessage(),
        Collections.emptyMap(),
        ex.getClass().getSimpleName(),
        status.value()
    );
    return ResponseEntity.status(status).body(errorResponse);
  }

}
