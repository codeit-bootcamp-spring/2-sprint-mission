package com.sprint.mission.discodeit.exception.security;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public abstract class TokenException extends DiscodeitException {

  protected TokenException(ErrorCode errorCode) {
    super(errorCode);
  }

  protected TokenException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }

}
