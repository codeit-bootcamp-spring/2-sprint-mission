package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.NotificationType;
import java.time.Instant;
import java.util.UUID;

public record NotificationDto(
    UUID id,
    Instant createdAt,
    UUID receiverId,
    String Title,
    String content,
    NotificationType type,
    UUID targetID
) {

}
