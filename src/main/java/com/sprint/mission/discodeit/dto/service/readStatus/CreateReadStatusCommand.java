package com.sprint.mission.discodeit.dto.service.readStatus;

import java.time.Instant;
import java.util.UUID;

public record CreateReadStatusCommand(
    UUID userId,
    UUID channelId,
    Instant lastReadAt,
    boolean notificationEnabled
) {

}
