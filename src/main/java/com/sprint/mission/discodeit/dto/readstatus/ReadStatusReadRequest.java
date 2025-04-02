package com.sprint.mission.discodeit.dto.readstatus;

import java.util.UUID;

public record ReadStatusReadRequest (
        UUID userId,
        UUID channelId
) {
}
