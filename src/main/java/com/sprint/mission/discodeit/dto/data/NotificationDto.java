package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.NotificationType;
import java.time.Instant;
import java.util.UUID;

public record NotificationDto(
    UUID id,
    NotificationType type,
    UUID targetId,
    Instant createdAt
) {
}
