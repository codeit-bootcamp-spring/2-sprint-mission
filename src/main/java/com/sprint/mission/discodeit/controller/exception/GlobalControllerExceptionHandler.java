package com.sprint.mission.discodeit.controller.exception;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalControllerExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleValidationErrors(MethodArgumentNotValidException ex) {
    String errorMessage = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.joining(", "));

    return ResponseEntity.badRequest().body(errorMessage);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
    log.error("Illegal argument: {}", ex.getMessage());
    return ResponseEntity.badRequest()
        .body(ErrorResponse.of("Bad Request", ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorResponse> handleNotSpecificException(Exception ex) {
    log.error("Not SpecificException: {}", ex.getMessage());
    return ResponseEntity.badRequest()
        .body(ErrorResponse.of("Bad Request", ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value()));
  }
}
