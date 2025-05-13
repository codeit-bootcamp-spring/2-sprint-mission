package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

  private final Instant timestamp;
  private final ErrorCode errorCode;
  private final Map<String, Object> details;

  public DiscodeitException(ErrorCode errorCode) {
    this(errorCode, errorCode.getMessage(), Collections.emptyMap());
  }

  public DiscodeitException(ErrorCode errorCode, String message) {
    this(errorCode, message, Collections.emptyMap());
  }

  public DiscodeitException(ErrorCode errorCode, String message, Map<String, Object> details) {
    super(message != null ? message : errorCode.getMessage());
    this.timestamp = Instant.now();
    this.errorCode = errorCode;
    this.details = details != null ? details : Collections.emptyMap();
  }
}
