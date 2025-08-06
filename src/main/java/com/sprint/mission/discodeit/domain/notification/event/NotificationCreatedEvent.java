package com.sprint.mission.discodeit.domain.notification.event;

import com.sprint.mission.discodeit.domain.notification.dto.NotificationResult;
import com.sprint.mission.discodeit.domain.notification.entity.Notification;

public record NotificationCreatedEvent(
    NotificationResult notificationResult
) {

  public static NotificationCreatedEvent from(Notification notification) {
    return new NotificationCreatedEvent(NotificationResult.from(notification));
  }

}
