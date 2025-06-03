package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

  private final Instant timestamp;
  private final ErrorCode errorCode;

  public DiscodeitException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.timestamp = Instant.now();
    this.errorCode = errorCode;
  }
}
