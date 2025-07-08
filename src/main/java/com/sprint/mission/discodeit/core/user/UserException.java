package com.sprint.mission.discodeit.core.user;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserException extends DiscodeitException {

  public UserException(ErrorCode errorCode, Object... args) {
    super(errorCode, args);
  }
}
