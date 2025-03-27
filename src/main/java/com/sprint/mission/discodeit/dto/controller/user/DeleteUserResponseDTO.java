package com.sprint.mission.discodeit.dto.controller.user;

import java.util.UUID;

public record DeleteUserResponseDTO(
        UUID id,
        String message
) {
}
