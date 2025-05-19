package com.sprint.mission.discodeit.dto.response;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;

@Getter
public class ErrorResponse {
  Instant timestamp;
  String code;
  String message;
  Map<String, Object> details;
  String exceptionType;
  int status;

  public ErrorResponse(Instant timestamp, String code, String message, Map<String, Object> details, String exceptionType, int status) {
    this.timestamp = timestamp;
    this.code = code;
    this.message = message;
    this.details = details;
    this.exceptionType = exceptionType;
    this.status = status;
  }

}
