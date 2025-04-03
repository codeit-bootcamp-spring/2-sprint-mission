package com.sprint.mission.discodeit.dto.common;

public record ApiResponse<T>(
        String message,
        T data
) {
}
