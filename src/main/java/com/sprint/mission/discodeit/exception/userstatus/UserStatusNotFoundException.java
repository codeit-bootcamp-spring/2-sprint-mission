package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserStatusNotFoundException extends UserStatusException {

  private static final ErrorCode DEFAULT_ERROR_CODE = ErrorCode.USER_STATUS_NOT_FOUND;

  public UserStatusNotFoundException(Throwable cause, Map<String, Object> details) {
    super(cause, DEFAULT_ERROR_CODE, details);
  }

  public UserStatusNotFoundException(Map<String, Object> details) {
    super(DEFAULT_ERROR_CODE, details);
  }

  public UserStatusNotFoundException() {
    super(DEFAULT_ERROR_CODE);
  }

  public UserStatusNotFoundException(Throwable cause) {
    super(DEFAULT_ERROR_CODE, cause);
  }
}
