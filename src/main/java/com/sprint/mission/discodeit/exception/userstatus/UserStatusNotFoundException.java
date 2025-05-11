package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class UserStatusNotFoundException extends UserStatusException {

  public UserStatusNotFoundException() {
    super(ResultCode.USER_STATUS_NOT_FOUND);
  }

  public UserStatusNotFoundException(Map<String, Object> details) {
    super(ResultCode.USER_STATUS_NOT_FOUND, details);
  }
}
