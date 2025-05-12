package com.sprint.mission.discodeit.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(
      DiscodeitException e, HttpServletRequest request) {

    ErrorCode errorCode = e.getErrorCode();

    ErrorResponse response = ErrorResponse.builder()
        .timestamp(e.getTimestamp())
        .code(errorCode.name())
        .message(errorCode.getMessage())
        .details(e.getDetails())
        .exceptionType(e.getClass().getSimpleName())
        .status(errorCode.getStatus().value())
        .build();

    return ResponseEntity.status(errorCode.getStatus()).body(response);
  }
}