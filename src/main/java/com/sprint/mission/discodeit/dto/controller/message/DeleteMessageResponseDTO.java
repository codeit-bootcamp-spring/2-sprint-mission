package com.sprint.mission.discodeit.dto.controller.message;

import java.util.UUID;

public record DeleteMessageResponseDTO(
        UUID id,
        String message
) {
}
