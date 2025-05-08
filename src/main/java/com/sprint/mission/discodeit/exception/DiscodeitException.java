package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;

public abstract class DiscodeitException extends RuntimeException {

  private final Instant timestamp = Instant.now();
  private final ErrorCode errorCode;
  private final Map<String, Object> details;

  protected DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.details = details;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public Map<String, Object> getDetails() {
    return details;
  }
}