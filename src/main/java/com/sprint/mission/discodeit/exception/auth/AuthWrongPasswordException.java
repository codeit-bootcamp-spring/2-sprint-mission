package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class AuthWrongPasswordException extends AuthException {

  private static final ErrorCode DEFAULT_ERROR_CODE = ErrorCode.AUTH_WRONG_PASSWORD;

  public AuthWrongPasswordException(Throwable cause, Map<String, Object> details) {
    super(cause, DEFAULT_ERROR_CODE, details);
  }

  public AuthWrongPasswordException(Map<String, Object> details) {
    super(DEFAULT_ERROR_CODE, details);
  }

  public AuthWrongPasswordException() {
    super(DEFAULT_ERROR_CODE);
  }

  public AuthWrongPasswordException(Throwable cause) {
    super(DEFAULT_ERROR_CODE, cause);
  }
}
