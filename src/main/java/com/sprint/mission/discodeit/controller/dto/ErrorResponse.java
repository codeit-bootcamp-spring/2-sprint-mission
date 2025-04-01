package com.sprint.mission.discodeit.controller.dto;

public record ErrorResponse(
        boolean success,
        String message
) {
    public static ErrorResponse of(String message) {
        return new ErrorResponse(false, message);
    }
}