package com.sprint.mission.discodeit.dto.service.notification;

import com.sprint.mission.discodeit.entity.NotificationType;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CreateNotificationParam(
    UUID receiverId,
    String title,
    String content,
    NotificationType type,
    UUID targetId
) {

}
