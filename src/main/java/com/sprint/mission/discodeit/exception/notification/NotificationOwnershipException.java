package com.sprint.mission.discodeit.exception.notification;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class NotificationOwnershipException extends NotificationException {

  public NotificationOwnershipException() {
    super(ErrorCode.NOTIFICATION_ACCESS_DENIED);
  }

  public static NotificationOwnershipException withIds(UUID notificationId, UUID receiverId) {
    NotificationOwnershipException exception = new NotificationOwnershipException();
    exception.addDetail("notificationId", notificationId);
    exception.addDetail("receiverId", receiverId);
    return exception;
  }
}