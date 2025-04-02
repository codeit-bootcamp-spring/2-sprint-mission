package com.sprint.mission.discodeit.dto.common;

public record ApiResponse<T>(
        boolean success,
        String message,
        T response
) {
}
