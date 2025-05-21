package com.sprint.mission.discodeit.dto.response;

import lombok.Builder;

import java.time.Instant;
import java.util.Map;

@Builder
public record ErrorResponse(
        Instant timestamp,
        String code,
        String message,
        Map<String, Object> details,
        String exceptionType,
        int status
) {}