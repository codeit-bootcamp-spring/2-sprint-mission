package com.sprint.mission.discodeit.dto.user;

import java.util.Optional;
import java.util.UUID;

public record UserCreateDto(
        String username,
        String password,
        String email,
        Optional<UUID> profileId
) {
}
