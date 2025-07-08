package com.sprint.mission.discodeit.core.message;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class MessageException extends DiscodeitException {

  public MessageException(ErrorCode errorCode, Object... args) {
    super(errorCode, args);
  }
}
