package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class DuplicateUserStatusException extends UserStatusException {

  public DuplicateUserStatusException() {
    super(ErrorCode.DUPLICATE_USER_STATUS);
  }

  public DuplicateUserStatusException(Map<String, Object> details) {
    super(ErrorCode.DUPLICATE_USER_STATUS, details);
  }
}
