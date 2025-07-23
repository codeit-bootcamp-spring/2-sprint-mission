package com.sprint.mission.discodeit.common.event.event;

import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.notification.entity.NotificationType;
import java.util.UUID;

public class AsyncFailedNotificationEvent extends NotificationEvent {

  public AsyncFailedNotificationEvent(UUID receiverId, BinaryContent binaryContent) {
    super(
        receiverId,
        NotificationType.ASYNC_FAILED, null,
        NotificationType.ASYNC_FAILED.getTitle(),
        createContent(binaryContent)
    );
  }

  private static String createContent(BinaryContent binaryContent) {
    String rawContent = String.format(binaryContent.getFileName());
    return NotificationType.ASYNC_FAILED.formatContent(rawContent);
  }

}
