package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UpdateUserDTO(
        UUID userId,
        String userName,
        String email,
        String password,
        UUID profileId
) {
}
