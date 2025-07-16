package com.sprint.mission.discodeit.core.notification.dto;

import com.sprint.mission.discodeit.core.notification.entity.NotificationType;
import java.util.UUID;

public record NotificationEvent(
    UUID receiverId,
    String title,
    String content,
    NotificationType type,
    UUID targetId
) {

}
