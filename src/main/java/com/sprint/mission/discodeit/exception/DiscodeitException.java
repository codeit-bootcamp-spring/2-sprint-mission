package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.*;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

    final Instant timestamp;
    final ErrorCode errorCode;
    //조회 시도한 사용자의 ID 정보 추가정보
    final Map<String, Object> details;

    protected DiscodeitException(ErrorCode errorCode,
        Map<String, Object> details) {
        super(errorCode.getMessage());
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
        this.details = details;
    }

    protected DiscodeitException(ErrorCode errorCode,
        Map<String, Object> details, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
        this.details = details;
    }
}
