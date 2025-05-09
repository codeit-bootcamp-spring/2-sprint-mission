package com.sprint.mission.discodeit.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    return ResponseEntity.badRequest().body(new ResponseErrorBody(e));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ResponseErrorBody> handleSingleValidException(
      ConstraintViolationException e) {
    log.warn("ConstraintViolationException handled: {}", e.getMessage());
    return ResponseEntity.badRequest().body(new ResponseErrorBody(e));
  }

  // 예상하지 못한 예외만 error 로그 남김 (+ stack trace)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseErrorBody> handleUnexpectedException(Exception e) {
    log.error("User unexpectedException handled: ", e);
    return ResponseEntity.internalServerError().body(new ResponseErrorBody(e));
  }

}
