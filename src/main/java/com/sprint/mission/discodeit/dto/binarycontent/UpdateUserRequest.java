package com.sprint.mission.discodeit.dto.binarycontent;

public record UpdateUserRequest(
    String newUsername,
    String newEmail,
    String newPassword
) {

}
