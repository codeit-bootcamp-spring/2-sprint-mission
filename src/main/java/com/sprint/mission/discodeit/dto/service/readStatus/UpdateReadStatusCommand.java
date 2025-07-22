package com.sprint.mission.discodeit.dto.service.readStatus;

import java.time.Instant;
import java.util.UUID;

public record UpdateReadStatusCommand(
    UUID userId,
    Instant newLastReadAt,
    Boolean newNotificationEnabled
) {

}
