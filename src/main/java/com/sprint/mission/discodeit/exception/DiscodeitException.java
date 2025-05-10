package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

    private final Instant timestamp;
    private final ErrorCode errorCode;
    // 예외 상황에 대한 부가정보를 전달하기 위한 Map -> 따라서 단순한 오류일 경우에 null일 수도 있음
    private final Map<String, Object> details;

    public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
        // RuntimeException error 발생 시, errorcode의 메시지를 전달하여 출력하도록 하기 위함
        // 전달하지 않으면 RuntimeException 글자만 뜬다.
        super(errorCode.getMessage());
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
        this.details = details != null ? details : new HashMap<>();
    }
}
