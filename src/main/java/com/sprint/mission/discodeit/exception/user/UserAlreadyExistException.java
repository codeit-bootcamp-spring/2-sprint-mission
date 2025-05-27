package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserAlreadyExistException extends UserException {

  public UserAlreadyExistException() {
    super(ErrorCode.DUPLICATE_USER);
  }

  private UserAlreadyExistException(ErrorCode errorCode) {
    super(errorCode);
  }

  public static UserAlreadyExistException byEmail(String email) {
    UserAlreadyExistException exception = new UserAlreadyExistException(
        ErrorCode.DUPLICATE_USER_EMAIL);
    exception.addDetail("email", email);
    return exception;
  }

  public static UserAlreadyExistException byUsername(String username) {
    UserAlreadyExistException exception = new UserAlreadyExistException(
        ErrorCode.DUPLICATE_USER_USERNAME);
    exception.addDetail("username", username);
    return exception;
  }

}
