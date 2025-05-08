package com.sprint.mission.discodeit.core.user.exception;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.UserException;

public class UserLoginFailedException extends UserException {

  private final Object[] args;

  public UserLoginFailedException(ErrorCode errorCode, Object... args) {
    super(errorCode);
    this.args = args != null ? args : new Object[0];
  }

  public Object[] getArgs() {
    return args;
  }
}
