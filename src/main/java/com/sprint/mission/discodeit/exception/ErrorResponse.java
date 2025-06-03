package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import lombok.Builder;

@Builder
public record ErrorResponse(
    Instant timestamp, int status, String code, String message, String exceptionType
) {

  public static ErrorResponse of(Instant timestamp, ErrorCode errorCode, String message,
      String exceptionType) {
    return ErrorResponse.builder()
        .timestamp(timestamp)
        .status(errorCode.getHttpStatus())
        .code(errorCode.getCode())
        .message(message)
        .exceptionType(exceptionType)
        .build();
  }
}
