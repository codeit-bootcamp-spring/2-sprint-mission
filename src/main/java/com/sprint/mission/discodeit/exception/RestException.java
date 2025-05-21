package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import lombok.Getter;

@Getter
public class RestException extends RuntimeException {

  private final Instant timestamp;
  private final ResultCode resultCode;
  private final Map<String, Object> details;

  public RestException(ResultCode resultCode) {
    super(resultCode.getMessage());
    this.timestamp = Instant.now();
    this.resultCode = resultCode;
    this.details = Collections.emptyMap();
  }

  public RestException(ResultCode resultCode, Map<String, Object> details) {
    super(resultCode.getMessage());
    this.timestamp = Instant.now();
    this.resultCode = resultCode;
    this.details = details;
  }
}
