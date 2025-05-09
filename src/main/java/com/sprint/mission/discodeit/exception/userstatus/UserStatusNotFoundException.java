package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class UserStatusNotFoundException extends UserStatusException {
  public UserStatusNotFoundException() {
    super(ErrorCode.USER_STATUS_NOT_FOUND);
  }

  public UserStatusNotFoundException notFoundWithId(UUID id) {
    this.putDetails("id", id);
    return this;
  }

  public UserStatusNotFoundException notFoundWithUserId(UUID userId) {
    this.putDetails("userId", userId);
    return this;
  }
}
