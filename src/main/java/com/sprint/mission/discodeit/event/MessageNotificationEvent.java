package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.NotificationType;
import java.util.UUID;

public record MessageNotificationEvent (
    String title,
    String content,
    NotificationType type,
    UUID targetId
) {

}
