package com.sprint.mission.discodeit.exception;

public record ErrorResponse(
        int status,
        String message
) {
}
