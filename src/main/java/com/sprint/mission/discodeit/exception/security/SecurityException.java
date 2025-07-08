package com.sprint.mission.discodeit.exception.security;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class SecurityException extends DiscodeitException {
  public SecurityException(ErrorCode errorCode) {
    super(errorCode);
  }

  public SecurityException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }
}
