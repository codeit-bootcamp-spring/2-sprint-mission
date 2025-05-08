package com.sprint.mission.discodeit.core.user.exception;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.UserException;
import lombok.Getter;

@Getter
public class UserNotFoundException extends UserException {

  private final Object[] args;

  public UserNotFoundException(ErrorCode errorCode, Object... args) {
    super(errorCode);
    this.args = args;
  }

}
