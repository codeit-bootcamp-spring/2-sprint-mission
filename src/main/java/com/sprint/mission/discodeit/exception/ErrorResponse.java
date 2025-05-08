package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

  private final Instant timestamp;
  private final String code;
  private final String message;
  private final Map<String, Object> details;
  private final String exceptionType;
  private final int status;


  public static ErrorResponse of(String code, String message, Map<String, Object> details,
      String exceptionType, int status) {
    return ErrorResponse.builder()
        .timestamp(Instant.now())
        .code(code)
        .message(message)
        .details(details)
        .exceptionType(exceptionType)
        .status(status)
        .build();
  }

  public static ErrorResponse of(String code, String message, String exceptionType, int status) {
    return ErrorResponse.builder()
        .timestamp(Instant.now())
        .code(code)
        .message(message)
        .details(Map.of())
        .exceptionType(exceptionType)
        .status(status)
        .build();
  }
}
