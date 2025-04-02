package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserUpdateRequest(
        @Schema(description = "새로운 사용자명")
        String newUsername,

        @Schema(description = "새로운 이메일")
        String newEmail,

        @Schema(description = "새로운 비밀번호")
        String newPassword
) {
}
