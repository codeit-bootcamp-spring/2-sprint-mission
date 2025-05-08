package com.sprint.mission.discodeit.exception.common;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;

@Getter
public class ErrorResponse {

  private final Instant timestamp;
  private final String code;
  private final String message;
  private final Map<String, Object> details;
  private final String exceptionType;
  private final int status;

  public ErrorResponse(String code, String message, Map<String, Object> details,
      String exceptionType, int status) {
    this.timestamp = Instant.now();
    this.code = code;
    this.message = message;
    this.details = details;
    this.exceptionType = exceptionType;
    this.status = status;
  }
}