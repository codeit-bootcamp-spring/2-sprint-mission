package com.sprint.mission.discodeit.core.notification;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class NotificationException extends DiscodeitException {

  public NotificationException(ErrorCode errorCode, Object... args) {
    super(errorCode, args);
  }
}
