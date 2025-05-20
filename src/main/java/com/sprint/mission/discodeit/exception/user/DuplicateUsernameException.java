package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.base.DiscodeitException;
import com.sprint.mission.discodeit.exception.base.ErrorCode;
import java.util.Map;

public class DuplicateUsernameException extends DiscodeitException {

  public DuplicateUsernameException() {
    super(ErrorCode.USERNAME_ALREADY_EXISTS);
  }

  public DuplicateUsernameException(Map<String, Object> details) {
    super(ErrorCode.USERNAME_ALREADY_EXISTS, details);
  }
}
