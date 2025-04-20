package com.sprint.mission.discodeit.adapter.inbound.error;

import com.sprint.mission.discodeit.exception.ErrorCode;
import lombok.Builder;

@Builder
public record ErrorResponse(
    int status, String code, String message
) {

  public static ErrorResponse of(ErrorCode errorCode, String message) {
    return ErrorResponse.builder()
        .status(errorCode.getHttpStatus())
        .code(errorCode.getCode())
        .message(message)
        .build();
  }
}
