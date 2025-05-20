package com.sprint.mission.discodeit.exception.User;

import com.sprint.mission.discodeit.exception.Basic.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class UserNotFoundException extends UserException {
  public UserNotFoundException(UUID userId) {
    super(ErrorCode.USER_NOT_FOUND, Map.of("userId", userId));
  }
}