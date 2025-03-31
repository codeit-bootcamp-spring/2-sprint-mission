package com.sprint.mission.discodeit.dto.user;

public record CreateUserDTO(
        String userName,
        String email,
        String password
) {
}
