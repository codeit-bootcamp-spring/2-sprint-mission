package com.sprint.mission.discodeit.exception;

public record ResponseErrorBody(
        int status,
        String message
) {
}
