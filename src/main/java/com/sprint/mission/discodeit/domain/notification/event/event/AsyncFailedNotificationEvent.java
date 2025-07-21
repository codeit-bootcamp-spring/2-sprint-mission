package com.sprint.mission.discodeit.domain.notification.event.event;

import com.sprint.mission.discodeit.domain.notification.entity.NotificationType;
import java.util.UUID;

public class AsyncFailedNotificationEvent extends NotificationEvent {

  public AsyncFailedNotificationEvent(UUID receiverId, String contentKeyword) {
    super(
        receiverId,
        NotificationType.ASYNC_FAILED, null,
        NotificationType.ASYNC_FAILED.getTitle(),
        NotificationType.ASYNC_FAILED.formatContent(contentKeyword)
    );
  }

}
