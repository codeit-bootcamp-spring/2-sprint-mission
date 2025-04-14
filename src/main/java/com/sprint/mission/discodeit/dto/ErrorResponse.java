package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.exception.ErrorCode;

public record ErrorResponse(
        String code,
        String description
) {
    public ErrorResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }
}
