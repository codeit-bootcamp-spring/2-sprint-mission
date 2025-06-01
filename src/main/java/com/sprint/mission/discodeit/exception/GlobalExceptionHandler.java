package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
// Controller 공통 예외를 처리하는 핸들러 (중복 코드 감소)
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseErrorBody> handleBodyValidException(
      MethodArgumentNotValidException e) {
    log.warn("MethodArgumentNotValidException handled: {}", e.getMessage());

    Map<String, Object> validationErrors = new HashMap<>();
    e.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      validationErrors.put(fieldName, errorMessage);
    });

    ResponseErrorBody errorBody = new ResponseErrorBody(
        Instant.now(),
        HttpStatus.BAD_REQUEST.name(),
        HttpStatus.BAD_REQUEST.value(),
        "Request data validation failed",
        validationErrors,
        e.getClass().getSimpleName());
    return ResponseEntity.badRequest().body((errorBody));
  }

  // 예상하지 못한 예외만 error 로그 남김 (+ stack trace)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseErrorBody> handleUnexpectedException(Exception e) {
    log.error("UnexpectedException handled: ", e);
    ResponseErrorBody errorBody = new ResponseErrorBody(
        Instant.now(),
        HttpStatus.INTERNAL_SERVER_ERROR.name(),
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Unexpected error occurred",
        Map.of(),
        e.getClass().getSimpleName());
    return ResponseEntity.internalServerError().body(errorBody);
  }

}
