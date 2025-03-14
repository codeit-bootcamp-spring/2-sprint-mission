package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserCreateRequestDto(
        String username,
        String email,
        String password,
        UUID profileId
) {
}
