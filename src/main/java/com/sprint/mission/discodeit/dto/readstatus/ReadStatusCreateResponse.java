package com.sprint.mission.discodeit.dto.readstatus;

import java.util.UUID;

public record ReadStatusCreateResponse(
        UUID readStatusId,
        UUID senderId,
        UUID channelId
) {
}
