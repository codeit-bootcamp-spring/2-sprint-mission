package com.sprint.mission.discodeit.dto.common;

import java.time.Instant;
import java.util.Map;
import lombok.Builder;

@Builder
public record ErrorResponse(
    Instant timestamp,
    String code,
    String message,
    String exceptionType,
    int status,
    Map<String, Object> details
) {

}