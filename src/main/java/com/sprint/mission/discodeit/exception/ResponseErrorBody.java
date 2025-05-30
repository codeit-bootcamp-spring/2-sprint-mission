package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseErrorBody {

  private final Instant timestamp;
  private final String code;
  private final int status;
  private final String message;
  private final Map<String, Object> details;
  private final String exceptionType;

  public ResponseErrorBody(RestException e) {
    this.timestamp = Instant.now();
    this.code = e.getErrorCode().name();
    this.status = e.getErrorCode().getStatus();
    this.message = e.getMessage();
    this.details = e.getDetails();
    this.exceptionType = e.getClass().getSimpleName();
  }
}
