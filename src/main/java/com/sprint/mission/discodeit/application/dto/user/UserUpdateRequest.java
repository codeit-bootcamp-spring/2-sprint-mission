package com.sprint.mission.discodeit.application.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(@NotBlank String newUsername, @NotBlank String newEmail, @NotBlank String newPassword) {
}
