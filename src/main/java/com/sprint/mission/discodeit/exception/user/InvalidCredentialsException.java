package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.base.DiscodeitException;
import com.sprint.mission.discodeit.exception.base.ErrorCode;

public class InvalidCredentialsException extends DiscodeitException {

  public InvalidCredentialsException() {
    super(ErrorCode.INVALID_CREDENTIALS);
  }
}
