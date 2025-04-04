package com.sprint.mission.discodeit.controller.dto;

import java.util.HashMap;
import java.util.Map;
import org.springframework.validation.BindingResult;

public record ErrorResponse(
    boolean success,
    String message,
    Map<String, String> errors
) {

  public static ErrorResponse of(String message) {
    return new ErrorResponse(false, message, null);
  }

  public static ErrorResponse of(BindingResult bindingResult) {
    Map<String, String> errorsMap = new HashMap<>();

    bindingResult.getFieldErrors()
        .forEach(error -> errorsMap.put(error.getField(), error.getDefaultMessage()));
    return new ErrorResponse(false, "Validation failed", errorsMap);
  }
}