package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserEmailAlreadyExistsException extends UserException {

  private static final ErrorCode DEFAULT_ERROR_CODE = ErrorCode.USER_EMAIL_ALREADY_EXISTS;

  public UserEmailAlreadyExistsException(Throwable cause, Map<String, Object> details) {
    super(cause, DEFAULT_ERROR_CODE, details);
  }

  public UserEmailAlreadyExistsException(Map<String, Object> details) {
    super(DEFAULT_ERROR_CODE, details);
  }

  public UserEmailAlreadyExistsException() {
    super(DEFAULT_ERROR_CODE);
  }

  public UserEmailAlreadyExistsException(Throwable cause) {
    super(DEFAULT_ERROR_CODE, cause);
  }
}
