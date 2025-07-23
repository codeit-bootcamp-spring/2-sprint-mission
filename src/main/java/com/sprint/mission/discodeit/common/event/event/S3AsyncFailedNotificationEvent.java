package com.sprint.mission.discodeit.common.event.event;

import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.notification.entity.NotificationType;
import java.util.UUID;

public class S3AsyncFailedNotificationEvent extends NotificationEvent {

  private static final String S3_ASYNC = "S3";

  public S3AsyncFailedNotificationEvent(UUID receiverId, BinaryContent binaryContent) {
    super(
        receiverId,
        NotificationType.ASYNC_FAILED, null,
        NotificationType.ASYNC_FAILED.getTitle(),
        createContent(binaryContent)
    );
  }

  private static String createContent(BinaryContent binaryContent) {
    String rawContent = String.format("%s%s", S3_ASYNC, binaryContent.getFileName());
    return NotificationType.ASYNC_FAILED.formatContent(rawContent);
  }

}
