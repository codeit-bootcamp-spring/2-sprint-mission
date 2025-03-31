package com.sprint.mission.discodeit.service.dto.user;

public record UserUpdateRequest(
        String newUsername,
        String newEmail,
        String newPassword
) {
}

