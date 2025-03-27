package com.sprint.mission.discodeit.dto.controller.user;

import java.util.UUID;

public record UpdateUserResponseDTO(
        UUID id,
        String message
) {
}
