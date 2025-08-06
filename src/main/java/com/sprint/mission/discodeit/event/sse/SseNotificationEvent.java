package com.sprint.mission.discodeit.event.sse;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import java.time.Instant;
import java.util.UUID;

public record SseNotificationEvent(
    UUID receiverId,
    NotificationDto notification,
    Instant timestamp,
    String requestId
) {

}
