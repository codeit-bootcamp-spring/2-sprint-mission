package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class UserNotFoundException extends UserException {

  public UserNotFoundException() {
    super(ResultCode.USER_NOT_FOUND);
  }

  public UserNotFoundException(Map<String, Object> details) {
    super(ResultCode.USER_NOT_FOUND, details);
  }
}
