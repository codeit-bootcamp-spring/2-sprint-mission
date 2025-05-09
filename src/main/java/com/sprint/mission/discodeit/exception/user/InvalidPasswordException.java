package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class InvalidPasswordException extends UserException {

  public InvalidPasswordException() {
    super(ResultCode.INVALID_PASSWORD);
  }

  public InvalidPasswordException(Map<String, Object> details) {
    super(ResultCode.INVALID_PASSWORD, details);
  }
}
