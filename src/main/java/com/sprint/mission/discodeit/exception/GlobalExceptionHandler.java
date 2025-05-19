package com.sprint.mission.discodeit.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
    ErrorResponse response = new ErrorResponse(e);
    return ResponseEntity.status(response.getStatus())
        .body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    DiscodeitException ex = new DiscodeitException(ErrorCode.INTERNAL_SERVER_ERROR, Map.of()) {
    };
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(ex));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex) {

    Map<String, Object> details = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error ->
        details.put(error.getField(), error.getDefaultMessage()));

    ErrorResponse response = ErrorResponse.of(ErrorCode.BAD_REQUEST, details,
        ex.getClass().getSimpleName());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }
}