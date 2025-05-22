package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

  private final Instant timestamp;
  private final ErrorCode errorCode;
  private final Map<String, Object> details;

  public DiscodeitException(Throwable cause, ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode.getMessage(), cause);
    this.timestamp = Instant.now();
    this.errorCode = errorCode;
    this.details = details;
  }

  public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
    this(null, errorCode, details);
  }

  public DiscodeitException(ErrorCode errorCode) {
    this(null, errorCode, null);
  }

  public DiscodeitException(ErrorCode errorCode, Throwable cause) {
    this(cause, errorCode, null);
  }
}
