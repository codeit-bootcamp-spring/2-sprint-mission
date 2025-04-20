package com.sprint.mission.discodeit.dto.service.user;

public record UserUpdateRequest(
    String newUsername,
    String newEmail,
    String newPassword
) {

}

