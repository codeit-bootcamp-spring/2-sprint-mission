package com.sprint.mission.discodeit.core.user.exception;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.UserException;

public class UserAlreadyExistsException extends UserException {

  private final Object[] args;

  public UserAlreadyExistsException(ErrorCode errorCode, Object... args) {
    super(errorCode);
    this.args = args;
  }

  public Object[] getArgs() {
    return args;
  }
}
