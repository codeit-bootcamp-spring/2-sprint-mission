package com.sprint.mission.discodeit.dto.user;

public record UpdateUserDTO(
        String userName,
        String email,
        String password
) {
}
