package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

  final Instant timestamp;
  final ErrorCode errorCode;
  final Map<String, Object> details;

  public DiscodeitException(ErrorCode errorCode) {
    super(errorCode.toString());
    this.timestamp = Instant.now();
    this.errorCode = errorCode;
    this.details = new HashMap<>();
  }

  public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode.toString());
    this.timestamp = Instant.now();
    this.errorCode = errorCode;
    this.details = details;
  }

}
