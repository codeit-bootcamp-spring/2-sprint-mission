package com.sprint.mission.discodeit.common.event.event;

import com.sprint.mission.discodeit.domain.notification.entity.NotificationType;
import java.util.UUID;

public class NewMessageNotificationEvent extends NotificationEvent {

  public NewMessageNotificationEvent(UUID receiverId, UUID channelId, String contentKeyword) {
    super(
        receiverId,
        NotificationType.NEW_MESSAGE,
        channelId,
        NotificationType.NEW_MESSAGE.getTitle(),
        NotificationType.NEW_MESSAGE.formatContent(contentKeyword)
    );
  }

}
