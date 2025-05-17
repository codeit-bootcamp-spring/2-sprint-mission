package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class NameAlreadyExistsException extends UserException {

  public NameAlreadyExistsException(String username) {
    super(ErrorCode.USER_ALREADY_EXISTS, Map.of("username", username));
  }
}
