package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "요청이 성공적으로 처리되었습니다.", data);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(true, "요청이 성공적으로 처리되었습니다.", null);
    }

    public static ApiResponse<?> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
