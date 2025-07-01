package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class SessionExpiredException extends AuthException {

  public SessionExpiredException(Map<String, Object> details) {
    super(ErrorCode.SESSION_EXPIRED, details);
  }
}
