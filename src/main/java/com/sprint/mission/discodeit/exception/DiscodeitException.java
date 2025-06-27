package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

  private final Object[] args;
  private final Instant timestamp;
  private final ErrorCode errorCode;
  private final Map<String, String> details;

  public DiscodeitException(ErrorCode errorCode, Object... args) {
    super(errorCode.getMessage());
    this.args = args;
    this.timestamp = Instant.now();
    this.errorCode = errorCode;
    this.details = Map.of();
  }
}
