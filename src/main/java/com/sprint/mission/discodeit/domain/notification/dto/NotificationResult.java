package com.sprint.mission.discodeit.domain.notification.dto;

import com.sprint.mission.discodeit.domain.notification.entity.Notification;
import com.sprint.mission.discodeit.domain.notification.entity.NotificationType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record NotificationResult(
    UUID id,
    Instant createdAt,
    UUID receiverId,
    String title,
    String content,
    NotificationType type,
    UUID targetId
) {

  public static NotificationResult from(Notification notification) {
    return new NotificationResult(
        notification.getId(),
        notification.getCreatedAt(),
        notification.getReceiverId(),
        notification.getTitle(),
        notification.getContent(),
        notification.getType(),
        notification.getTargetId()
    );
  }

  public static List<NotificationResult> from(List<Notification> notifications) {
    return notifications.stream()
        .map(NotificationResult::from)
        .toList();
  }

}
