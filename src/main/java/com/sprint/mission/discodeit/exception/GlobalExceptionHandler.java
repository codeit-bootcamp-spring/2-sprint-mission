package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.common.ErrorResponse;
import com.sprint.mission.discodeit.exception.base.DiscodeitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException ex) {
    return ResponseEntity.status(ex.getCode())
        .body(ErrorResponse.builder()
            .timestamp(ex.getTimestamp())
            .code(ex.getErrorCodeName())
            .message(ex.getMessage())
            .details(ex.getDetails())
            .exceptionType(ex.getExceptionType())
            .status(ex.getCode())
            .build());
  }
}
