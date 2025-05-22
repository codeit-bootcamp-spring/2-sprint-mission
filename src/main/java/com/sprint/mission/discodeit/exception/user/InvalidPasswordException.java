package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class InvalidPasswordException extends UserException {

  public InvalidPasswordException() {
    super(ErrorCode.INVALID_PASSWORD);
  }

  public InvalidPasswordException(Map<String, Object> details) {
    super(ErrorCode.INVALID_PASSWORD, details);
  }
}
