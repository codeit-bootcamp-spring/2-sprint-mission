package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.domain.NotificationType;
import java.util.Map;
import java.util.UUID;

public record NotificationEvent(
    UUID receiverId,
    NotificationType notificationType,
    UUID targetId,
    Map<String, Object> notificationInfo
) {

}
