package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ErrorDetailKey;
import java.util.Map;
import java.util.UUID;

public class UserStatusAlreadyExistsException extends UserStatusException {

  public UserStatusAlreadyExistsException(UUID userId, String message) {
    super(ErrorCode.USER_STATUS_ALREADY_EXIST, message,
        Map.of(ErrorDetailKey.USER_ID.getKey(), userId));
  }
}
