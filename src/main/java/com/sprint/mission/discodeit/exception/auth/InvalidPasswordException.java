package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ErrorDetailKey;
import java.util.Map;
import java.util.UUID;

public class InvalidPasswordException extends AuthException {

  public InvalidPasswordException(UUID userId) {
    super(ErrorCode.INVALID_PASSWORD, null, Map.of(ErrorDetailKey.USER_ID.getKey(), userId));
  }
}
