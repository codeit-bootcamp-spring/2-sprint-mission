package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserNameNotExistsException extends DiscodeitException {

  public UserNameNotExistsException(String username) {
    super(ErrorCode.USER_NAME_NOT_EXISTS, Map.of("username", username));
  }
}
