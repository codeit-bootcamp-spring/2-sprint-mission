package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record CreateUserDTO(
        String userName,
        String email,
        String password,
        UUID profileId
) {
}
