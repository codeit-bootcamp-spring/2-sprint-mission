package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class InvalidCredentialsException extends UserException {

  private InvalidCredentialsException(ErrorCode errorCode) {
    super(errorCode);
  }

  public static InvalidCredentialsException wrongPassword() {
    return new InvalidCredentialsException(ErrorCode.WRONG_PASSWORD);
  }

}
