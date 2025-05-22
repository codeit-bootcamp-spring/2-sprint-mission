package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class InvalidCredentialsException extends UserException {
  public InvalidCredentialsException() {
    super(ErrorCode.USER_INVALID_CREDENTIALS);
  }
}
