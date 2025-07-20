package com.sprint.mission.discodeit.common;

import com.sprint.mission.discodeit.entity.NotificationType;

import java.util.UUID;


public record NotificationEvent(
        UUID receiverId,
        NotificationType type,
        UUID targetId,
        String content
) {
}