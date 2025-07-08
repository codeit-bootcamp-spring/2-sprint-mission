package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import lombok.Builder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import software.amazon.awssdk.services.s3.endpoints.internal.Value.Str;

@Builder
public record ErrorResponse(
    Instant timestamp, int status, String code, String message, String exceptionType,
    Map<String, String> details
) {

  public static ErrorResponse from(DiscodeitException ex, String message) {
    ErrorCode errorCode = ex.getErrorCode();
    return ErrorResponse.builder()
        .timestamp(ex.getTimestamp())
        .status(errorCode.getHttpStatus())
        .code(errorCode.getCode())
        .message(message)
        .exceptionType(ex.getClass().getSimpleName())
        .details(ex.getDetails())
        .build();
  }

  public static ErrorResponse from(MethodArgumentNotValidException ex, ErrorCode errorCode,
      String message) {
    return ErrorResponse.builder()
        .timestamp(Instant.now())
        .status(errorCode.getHttpStatus())
        .code(errorCode.getCode())
        .message(message)
        .exceptionType(ex.getClass().getSimpleName())
        .details(Map.of())
        .build();
  }


}
