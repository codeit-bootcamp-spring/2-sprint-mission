package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class PasswordMismatchException extends AuthException {

  public PasswordMismatchException(String password) {
    super(ErrorCode.PASSWORD_MISMATCH, Map.of("password", password));
  }
}
