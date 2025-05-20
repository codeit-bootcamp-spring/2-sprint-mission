package com.sprint.mission.discodeit.dto.error;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String code,
        String message,
        Map<String, Object> details,
        String exceptionType
) {}
