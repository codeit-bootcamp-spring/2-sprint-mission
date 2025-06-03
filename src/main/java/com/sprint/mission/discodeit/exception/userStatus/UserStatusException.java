package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public abstract class UserStatusException extends DiscodeitException {

  protected UserStatusException(ErrorCode errorCode) {
    super(errorCode);
  }

  protected UserStatusException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }
}
