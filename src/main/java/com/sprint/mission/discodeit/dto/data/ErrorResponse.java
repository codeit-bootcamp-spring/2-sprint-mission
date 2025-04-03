package com.sprint.mission.discodeit.dto.data;

public record ErrorResponse(
        boolean success,
        String message
) {
    public static ErrorResponse of(boolean success, String message) {
        return new ErrorResponse(success, message);
    }
}
