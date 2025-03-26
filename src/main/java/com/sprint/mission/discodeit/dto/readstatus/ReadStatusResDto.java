package com.sprint.mission.discodeit.dto.readstatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResDto(
        UUID id,
        UUID userId,
        UUID channelId,
        Instant lastReadAt) {
}
