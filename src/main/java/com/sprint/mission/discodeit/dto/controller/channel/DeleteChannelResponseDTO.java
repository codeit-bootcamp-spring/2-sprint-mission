package com.sprint.mission.discodeit.dto.controller.channel;

import java.util.UUID;

public record DeleteChannelResponseDTO(
        UUID id,
        String message
) {
}
