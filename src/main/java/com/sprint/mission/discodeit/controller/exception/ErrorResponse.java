package com.sprint.mission.discodeit.controller.exception;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

public record ErrorResponse(String error, String message, int status, String timestamp) {

  public static ErrorResponse of(String error, String message, int status) {
    return new ErrorResponse(error, message, status, LocalDateTime.now().toString());
  }

  public static List<ErrorResponse> of(List<FieldError> fieldErrors) {
    return fieldErrors.stream()
        .map(fieldError -> new ErrorResponse(fieldError.getField(),
            fieldError.getDefaultMessage(), HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().toString()))
        .toList();
  }
}
