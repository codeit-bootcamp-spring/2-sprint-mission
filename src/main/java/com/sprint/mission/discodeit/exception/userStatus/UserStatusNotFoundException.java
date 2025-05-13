package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ErrorDetailKey;
import java.util.Map;
import java.util.UUID;

public class UserStatusNotFoundException extends UserStatusException {

  public UserStatusNotFoundException(UUID userStatusId) {
    super(ErrorCode.USER_STATUS_NOT_FOUND, null,
        Map.of(ErrorDetailKey.USER_STATUS_ID.getKey(), userStatusId));
  }

  public UserStatusNotFoundException(UUID userStatusId, String message) {
    super(ErrorCode.USER_STATUS_NOT_FOUND, message,
        Map.of(ErrorDetailKey.USER_STATUS_ID.getKey(), userStatusId));
  }
}
