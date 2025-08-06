package com.sprint.mission.discodeit.domain.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class UserNotFoundException extends UserException {

  public UserNotFoundException() {
    super(ErrorCode.USER_NOT_FOUND);
  }

  public static UserNotFoundException withId(UUID userId) {
    UserNotFoundException exception = new UserNotFoundException();
    exception.addDetail("userId", userId);
    return exception;
  }

  public static UserNotFoundException withUsername(String username) {
    UserNotFoundException exception = new UserNotFoundException();
    exception.addDetail("username", username);
    return exception;
  }
} 