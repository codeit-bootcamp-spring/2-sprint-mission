package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.constant.ErrorCode;
import java.time.Instant;
import java.util.Map;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

    private final Instant timestamp = Instant.now();
    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
        this.errorCode = errorCode;
        this.details = details;
    }

    public DiscodeitException(ErrorCode errorCode, Map<String, Object> details, Throwable cause) {
        super(errorCode.name(), cause);
        this.errorCode = errorCode;
        this.details = details;
    }
}
