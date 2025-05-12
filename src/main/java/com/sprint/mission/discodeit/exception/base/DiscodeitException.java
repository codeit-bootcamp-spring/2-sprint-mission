package com.sprint.mission.discodeit.exception.base;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

  private final ErrorCode errorCode;
  private final Instant timestamp;
  private final Map<String, Object> details;

  public DiscodeitException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.timestamp = Instant.now();
    this.details = null;
  }

  public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.timestamp = Instant.now();
    this.details = details;
  }

  @Override
  public String getMessage() {
    return this.errorCode.getMessage();
  }

  public String getErrorCodeName() {
    return errorCode.name();
  }

  public int getCode() {
    return this.errorCode.getCode();
  }

  public String getExceptionType() {
    return this.getClass().getSimpleName();
  }
}
