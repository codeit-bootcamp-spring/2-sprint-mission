package com.sprint.mission.discodeit.domain.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class InvalidCredentialsException extends UserException {

  public InvalidCredentialsException() {
    super(ErrorCode.INVALID_USER_CREDENTIALS);
  }

  public static InvalidCredentialsException wrongPassword() {
    InvalidCredentialsException exception = new InvalidCredentialsException();
    return exception;
  }
} 