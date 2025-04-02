package com.sprint.mission.discodeit.dto.common;

public record ErrorResponse(
        int statusCode,
        String message
) {
}