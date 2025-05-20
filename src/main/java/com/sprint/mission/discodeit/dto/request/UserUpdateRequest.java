package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Schema(description = "새로운 사용자명")
        @Size(min = 2, max = 30, message = "사용자명은 2~30자 사이여야 합니다.")
        String newUsername,

        @Schema(description = "새로운 이메일")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String newEmail,

        @Schema(description = "새로운 비밀번호")
        @Size(min = 8, max = 100, message = "비밀번호는 8~100자 사이여야 합니다.")
        String newPassword
) {
}
