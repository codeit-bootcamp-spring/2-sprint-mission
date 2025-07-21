package com.sprint.mission.discodeit.domain.notification.event.event;

import com.sprint.mission.discodeit.domain.notification.entity.NotificationType;
import java.util.UUID;

public class RoleChangeNotificationEvent extends NotificationEvent {

  public RoleChangeNotificationEvent(UUID receiverId, String contentKeyword) {
    super(
        receiverId,
        NotificationType.ROLE_CHANGED,
        receiverId,
        NotificationType.ROLE_CHANGED.getTitle(),
        NotificationType.ROLE_CHANGED.formatContent(contentKeyword)
    );
  }

}
