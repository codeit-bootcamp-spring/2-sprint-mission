package com.sprint.mission.discodeit.dto;

public record BaseResponseDto(
        boolean success,
        String message
) {
    public static BaseResponseDto success(String message) {
        return new BaseResponseDto(true, message);
    }

    public static BaseResponseDto failure(String message) {
        return new BaseResponseDto(false, message);
    }
}
