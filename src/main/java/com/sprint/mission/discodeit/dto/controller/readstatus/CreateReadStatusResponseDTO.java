package com.sprint.mission.discodeit.dto.controller.readstatus;

import java.util.UUID;

public record CreateReadStatusResponseDTO(
        UUID id,
        UUID userId,
        UUID channelId
) {
}
