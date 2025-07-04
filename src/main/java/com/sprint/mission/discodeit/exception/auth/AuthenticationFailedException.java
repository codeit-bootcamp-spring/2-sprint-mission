package com.sprint.mission.discodeit.exception.auth;


import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class AuthenticationFailedException extends AuthException {

  public AuthenticationFailedException(Map<String, Object> details) {
    super(ErrorCode.AUTHENTICATION_FAILED, details);
  }
}