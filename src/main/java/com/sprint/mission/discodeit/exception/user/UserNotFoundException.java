package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserNotFoundException extends UserException {

  private static final ErrorCode DEFAULT_ERROR_CODE = ErrorCode.USER_NOT_FOUND;

  public UserNotFoundException(Throwable cause, Map<String, Object> details) {
    super(cause, DEFAULT_ERROR_CODE, details);
  }

  public UserNotFoundException(Map<String, Object> details) {
    super(DEFAULT_ERROR_CODE, details);
  }

  public UserNotFoundException() {
    super(DEFAULT_ERROR_CODE);
  }

  public UserNotFoundException(Throwable cause) {
    super(DEFAULT_ERROR_CODE, cause);
  }
}
