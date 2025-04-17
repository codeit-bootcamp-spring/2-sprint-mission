package com.sprint.mission.discodeit.dto.service.readStatus;

import java.time.Instant;
import java.util.UUID;

public record FindReadStatusResult(
    UUID id,
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

}
