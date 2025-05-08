package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import lombok.Builder;

@Builder
public record ApiErrorResponse(
    int status,
    String error,
    String message,
    String path,
    Instant timestamp
) {}