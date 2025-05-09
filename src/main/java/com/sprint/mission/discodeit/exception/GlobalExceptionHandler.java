package com.sprint.mission.discodeit.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleException(DiscodeitException e) {
    log.error("예외 발생: ErrorCode={}", e.getErrorCode(), e);
    HttpStatus status = e.getErrorCode().getHttpStatus();
    ErrorResponse response = new ErrorResponse(e, status.value());
    return ResponseEntity
        .status(status)
        .body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    ErrorResponse response = new ErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR.value());
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
    log.error("요청의 유효성 검사 실패: message={}", e.getMessage());
    Map<String, Object> errors = new HashMap<>();
    e.getBindingResult().getAllErrors().forEach(error -> {
      String errorField = ((FieldError) error).getField();
      errors.put(errorField, error.getDefaultMessage());
    });

    ErrorResponse response = new ErrorResponse(e, errors, HttpStatus.BAD_REQUEST.value());
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST.value())
        .body(response);
  }
}
