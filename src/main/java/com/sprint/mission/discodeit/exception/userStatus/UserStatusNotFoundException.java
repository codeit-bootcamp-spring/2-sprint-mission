package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class UserStatusNotFoundException extends UserStatusException {

  public UserStatusNotFoundException() {
    super(ErrorCode.USER_STATUS_NOT_FOUND);
  }

  public static UserStatusNotFoundException byId(UUID userStatusId) {
    UserStatusNotFoundException exception = new UserStatusNotFoundException();
    exception.addDetail("userStatusId", userStatusId);
    return exception;
  }

  public static UserStatusNotFoundException byUserId(UUID userId) {
    UserStatusNotFoundException exception = new UserStatusNotFoundException();
    exception.addDetail("userId", userId);
    return exception;
  }
  
}
