package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserNameAlreadyExistsException extends UserException {

  private static final ErrorCode DEFAULT_ERROR_CODE = ErrorCode.USER_NAME_ALREADY_EXISTS;

  public UserNameAlreadyExistsException(Throwable cause, Map<String, Object> details) {
    super(cause, DEFAULT_ERROR_CODE, details);
  }

  public UserNameAlreadyExistsException(Map<String, Object> details) {
    super(DEFAULT_ERROR_CODE, details);
  }

  public UserNameAlreadyExistsException() {
    super(DEFAULT_ERROR_CODE);
  }

  public UserNameAlreadyExistsException(Throwable cause) {
    super(DEFAULT_ERROR_CODE, cause);
  }
}
