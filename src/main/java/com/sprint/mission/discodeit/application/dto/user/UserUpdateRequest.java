package com.sprint.mission.discodeit.application.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "사용자 수정 요청")
public record UserUpdateRequest(
        @NotBlank String newUsername,
        @NotBlank String newEmail,
        @NotBlank String newPassword) {
}
