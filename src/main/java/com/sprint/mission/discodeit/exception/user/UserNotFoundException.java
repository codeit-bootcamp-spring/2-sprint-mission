package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ErrorDetailKey;
import java.util.Map;
import java.util.UUID;

public class UserNotFoundException extends UserException {

  public UserNotFoundException(UUID userId) {
    super(ErrorCode.USER_NOT_FOUND, null, Map.of(ErrorDetailKey.USER_ID.getKey(), userId));
  }

  public UserNotFoundException(String username) {
    super(ErrorCode.USER_NOT_FOUND, null, Map.of(ErrorDetailKey.USER_NAME.getKey(), username));
  }

}
