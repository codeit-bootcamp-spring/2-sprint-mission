package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class DuplicateUserStatusException extends UserStatusException {

  public DuplicateUserStatusException() {
    super(ResultCode.DUPLICATE_USER_STATUS);
  }

  public DuplicateUserStatusException(Map<String, Object> details) {
    super(ResultCode.DUPLICATE_USER_STATUS, details);
  }
}
