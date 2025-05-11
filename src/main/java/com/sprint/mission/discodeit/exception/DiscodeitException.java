package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;

public class DiscodeitException extends RuntimeException {

    @Getter
    private final Instant timestamp;
    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    // 간단한 경우 -> 에러코드만 던질때
    public DiscodeitException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
        this.details = Map.of();
    }

    // 상세 정보가 필요한 경우 -> 에러코드 + 추가 정보
    public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode.getMessage());
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
        this.details = details;
    }

}
