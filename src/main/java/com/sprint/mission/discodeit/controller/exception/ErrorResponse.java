package com.sprint.mission.discodeit.controller.exception;

import java.time.LocalDateTime;

public record ErrorResponse(String error, String message, int status, String timestamp) {
    public static ErrorResponse of(String error, String message, int status) {
        return new ErrorResponse(error, message, status, LocalDateTime.now().toString());
    }
}
