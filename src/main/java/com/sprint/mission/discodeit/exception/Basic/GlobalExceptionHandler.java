package com.sprint.mission.discodeit.exception.Basic;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeit(DiscodeitException e) {
    ErrorCode error = e.getErrorCode();

    ErrorResponse re = ErrorResponse.builder()
            .timestamp(e.getTimestamp())
            .code(error.name()).message(error.getMessage())
            .details(e.getDetails())
            .exceptionType(e.getClass().getSimpleName())
            .status(error.getStatus())
            .build();

    return ResponseEntity
        .status(error.getStatus())
        .body(re);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, Object> details = new HashMap<>();

    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      details.put(fieldName, errorMessage);
    });

    ErrorResponse re = ErrorResponse.builder()
            .timestamp(Instant.now())
            .code("VALIDATION_ERROR")
            .message("Request validation failed")
            .details(details)
            .exceptionType(ex.getClass().getSimpleName())
            .status(HttpStatus.BAD_REQUEST.value())
            .build();

    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(re);
  }


}
