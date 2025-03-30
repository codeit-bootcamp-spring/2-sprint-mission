package com.sprint.mission.discodeit.dto.readStatusService;

import java.util.UUID;

public record ReadStatusCreateRequest(
        UUID userId,
        UUID channelId
) {
}
