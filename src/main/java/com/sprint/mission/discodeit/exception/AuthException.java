package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class AuthException extends DiscodeitException {

  public AuthException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  public static AuthException invalidPassword(Map<String, Object> details) {
    return new AuthException(ErrorCode.INVALID_USER_PASSWORD, details);
  }

}
