package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;

public class ErrorResponse {

  private final Instant timestamp;
  private final String code;
  private final String message;
  private final Map<String, Object> details;
  private final String exceptionType;
  private final int status;

  public ErrorResponse(DiscodeitException ex, int status) {
    this.timestamp = ex.getTimestamp();
    this.code = ex.getErrorCode().name();
    this.message = ex.getErrorCode().getMessage();
    this.details = ex.getDetails();
    this.exceptionType = ex.getClass().getSimpleName();
    this.status = status;
  }

  public ErrorResponse(Instant timestamp, String code, String message, Map<String, Object> details,
      String exceptionType, int status) {
    this.timestamp = timestamp;
    this.code = code;
    this.message = message;
    this.details = details;
    this.exceptionType = exceptionType;
    this.status = status;

  }

  // Getter methods (생략 가능 - 필요 시 Lombok @Getter 사용 가능)
  public Instant getTimestamp() {
    return timestamp;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public Map<String, Object> getDetails() {
    return details;
  }

  public String getExceptionType() {
    return exceptionType;
  }

  public int getStatus() {
    return status;
  }
}
