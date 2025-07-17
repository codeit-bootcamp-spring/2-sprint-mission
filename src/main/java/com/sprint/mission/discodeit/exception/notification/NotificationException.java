package com.sprint.mission.discodeit.exception.notification;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.RestException;
import java.util.Map;

public class NotificationException extends RestException {

  public NotificationException(ErrorCode errorCode) {
    super(errorCode);
  }

  public NotificationException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
