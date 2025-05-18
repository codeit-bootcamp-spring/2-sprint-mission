package com.sprint.mission.discodeit.exception.response;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;

public record ErrorResponse(
    Instant timestamp,
    String code,
    String message,
    Map<String, Object> details,
    String exceptionType,
    int status
) {

  public static ErrorResponse of(ErrorCode errorCode, Map<String, Object> details,
      String exceptionType) {
    return new ErrorResponse(
        Instant.now(),
        errorCode.getCode(),
        errorCode.getMessage(), // 기본 메시지 사용
        (details == null || details.isEmpty()) ? null
            : Collections.unmodifiableMap(details), // null 또는 빈 경우 details도 null
        exceptionType,
        errorCode.getStatus().value()
    );
  }

  public static ErrorResponse of(ErrorCode errorCode, String customMessage,
      Map<String, Object> exceptionDetails, String exceptionType) {
    return new ErrorResponse(
        Instant.now(),
        errorCode.getCode(),
        customMessage, // 커스텀 메시지 사용
        (exceptionDetails == null || exceptionDetails.isEmpty()) ? null
            : Collections.unmodifiableMap(exceptionDetails),
        exceptionType,
        errorCode.getStatus().value()
    );
  }

  public static ErrorResponse of(ErrorCode errorCode, String exceptionType) {
    return new ErrorResponse(
        Instant.now(),
        errorCode.getCode(),
        errorCode.getMessage(),
        null, // details 없음
        exceptionType,
        errorCode.getStatus().value()
    );
  }

}
