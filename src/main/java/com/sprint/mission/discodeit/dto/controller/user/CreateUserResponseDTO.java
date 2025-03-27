package com.sprint.mission.discodeit.dto.controller.user;

import java.util.UUID;

public record CreateUserResponseDTO(
        UUID id,
        UUID profileId,
        String username,
        String email
) {
}
