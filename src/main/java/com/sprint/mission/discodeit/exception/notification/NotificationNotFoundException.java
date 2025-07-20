package com.sprint.mission.discodeit.exception.notification;

import java.util.UUID;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class NotificationNotFoundException extends NotificationException {

  public NotificationNotFoundException() {
    super(ErrorCode.NOTIFICATION_NOT_FOUND);
  }

  public static NotificationNotFoundException withId(UUID notificationId) {
    NotificationNotFoundException exception = new NotificationNotFoundException();
    exception.addDetail("notificationId", notificationId);
    return exception;
  }

  public static NotificationNotFoundException withReceiverId(UUID receiverId) {
    NotificationNotFoundException exception = new NotificationNotFoundException();
    exception.addDetail("receiverId", receiverId);
    return exception;
  }
}
