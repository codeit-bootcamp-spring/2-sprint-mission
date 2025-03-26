package com.sprint.mission.discodeit.controller.dto;

public record UserUpdateDataRequest(
        String newUsername,
        String newEmail,
        String newPassword
) {
}
