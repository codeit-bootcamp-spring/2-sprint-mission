package com.sprint.mission.discodeit.dto.user;

public record UserUpdateRequest(
    String newUsername,
    String newPassword,
    String newEmail
) {

}
