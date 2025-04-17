package com.sprint.mission.discodeit.dto.service.user;

public record UpdateUserCommand(
    String newUsername,
    String newEmail,
    String newPassword
) {

}
