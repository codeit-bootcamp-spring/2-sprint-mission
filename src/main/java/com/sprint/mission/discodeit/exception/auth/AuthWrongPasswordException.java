package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class AuthWrongPasswordException extends AuthException {

  public AuthWrongPasswordException(Map<String, Object> details) {
    super(Instant.now(), ErrorCode.WRONG_PASSWORD, details);
  }
}
