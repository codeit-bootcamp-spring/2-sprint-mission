package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.common.ErrorResponse;
import com.sprint.mission.discodeit.exception.base.DiscodeitException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
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

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    Map<String, Object> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(Instant.now())
        .code("VALIDATION_ERROR")
        .message("입력 데이터 유효성 검사에 실패했습니다.")
        .details(errors)
        .exceptionType(ex.getClass().getSimpleName())
        .status(HttpStatus.BAD_REQUEST.value())
        .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }
}
