package com.sprint.mission.discodeit.service.dto.userdto;

public record UserUpdateDto(
        String newUsername,
        String newEmail,
        String newPassword
) {

}
