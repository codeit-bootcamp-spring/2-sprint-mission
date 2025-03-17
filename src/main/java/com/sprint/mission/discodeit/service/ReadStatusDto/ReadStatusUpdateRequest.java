package com.sprint.mission.discodeit.service.ReadStatusDto;

import java.util.UUID;

public record ReadStatusUpdateRequest(
        UUID readStatusId,
        UUID channelId,
        UUID userId
        ) {
}
