package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.NotificationType;
import java.util.UUID;

public record NotificationEvent(
    UUID receiverId,
    NotificationType type,
    UUID targetId
) {}