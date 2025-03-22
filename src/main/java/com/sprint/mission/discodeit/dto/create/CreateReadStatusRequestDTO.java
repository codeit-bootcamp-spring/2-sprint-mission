package com.sprint.mission.discodeit.dto.create;

import java.util.UUID;

public record CreateReadStatusRequestDTO(
        UUID userId,
        UUID channelId
) {
}
