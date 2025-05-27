package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public abstract class MessageException extends DiscodeitException {

  protected MessageException(ErrorCode errorCode) {
    super(errorCode);
  }

  protected MessageException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }
}
