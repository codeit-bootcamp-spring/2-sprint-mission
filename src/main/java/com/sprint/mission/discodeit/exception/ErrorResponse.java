package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import org.springframework.data.convert.Jsr310Converters.InstantToDateConverter;

public record ErrorResponse(
    Instant timeStamp,
    String code,
    String message,
    Map<String, Object> details,
    // 발생한 예외의 클래스 이름
    String exceptionType,
    // HTTP 상태코드
    int status
) {

}
