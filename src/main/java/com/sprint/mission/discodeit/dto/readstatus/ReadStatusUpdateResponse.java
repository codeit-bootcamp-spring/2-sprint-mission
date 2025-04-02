package com.sprint.mission.discodeit.dto.readstatus;

import java.util.UUID;

public record ReadStatusUpdateResponse (
        UUID userId,
        UUID channelId
) {
}
