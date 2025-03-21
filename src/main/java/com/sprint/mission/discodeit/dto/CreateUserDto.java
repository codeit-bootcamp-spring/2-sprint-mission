package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record CreateUserDto(
        String userName,
        String pwd,
        String email,
        UUID profileImageKey
) {
}
