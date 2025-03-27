package com.sprint.mission.discodeit.dto.controller.readstatus;

import java.util.UUID;

public record CreateReadStatusRequestDTO(
        UUID userId,
        UUID channelId
) {
}
