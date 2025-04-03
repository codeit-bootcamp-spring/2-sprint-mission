package com.sprint.mission.discodeit.service.dto.userdto;

public record UserUpdateDto(
        String changeName,
        String changeEmail,
        String changePassword
) {

}
