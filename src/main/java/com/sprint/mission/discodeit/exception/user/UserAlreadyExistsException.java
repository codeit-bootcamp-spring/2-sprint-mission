package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserAlreadyExistsException extends UserException {
  public UserAlreadyExistsException() {
    super(ErrorCode.DUPLICATE_USER);
  }

  public UserAlreadyExistsException duplicateUsername(String username) {
    this.putDetails("username", username);
    return this;
  }

  public UserAlreadyExistsException duplicateEmail(String email) {
    this.putDetails("email", email);
    return this;
  }
}
