package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationDto(
    UUID id,
    LocalDateTime createdAt,
    UUID receiverId,
    String title,
    String content,
    NotificationType notificationType,
    UUID targetId

) {

    public static NotificationDto from(Notification notification) {
        return new NotificationDto(
            notification.getId(),
            notification.getCreatedAt(),
            notification.getReceiver().getId(),
            notification.getTitle(),
            notification.getContent(),
            notification.getType(),
            notification.getTargetId()
        );
    }


}
