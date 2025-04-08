package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusRequest(
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

}
