package com.sprint.mission.discodeit.exception;

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

  public ErrorResponse(DiscodeitException ex) {
    this.timestamp = ex.getTimestamp();
    this.code = ex.getErrorCode().name();
    this.message = ex.getErrorCode().getMessage();
    this.details = ex.getDetails();
    this.exceptionType = ex.getClass().getSimpleName();
    this.status = ex.getErrorCode().getStatus();
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

  public static ErrorResponse of(ErrorCode errorCode, Map<String, Object> details,
      String exceptionName) {
    return new ErrorResponse(
        Instant.now(),
        errorCode.name(),
        errorCode.getMessage(),
        details,
        exceptionName,
        errorCode.getStatus()
    );
  }
}
