package com.sprint.mission.discodeit.controller.dto.user;

public record UserUpdateDataRequest(
        String newUsername,
        String newEmail,
        String newPassword
) {
}
