package com.sprint.mission.discodeit.controller.dto;

public record ErrorResponse(
        boolean success,
        String message
) {
    public static ErrorResponse of(boolean success, String message) {
        return new ErrorResponse(success, message);
    }
}
