package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class AccessDeniedException extends AuthException {

  public AccessDeniedException() {
    super(ErrorCode.FORBIDDEN_ACCESS_DENIED);
  }

  public AccessDeniedException(Map<String, Object> details) {
    super(ErrorCode.FORBIDDEN_ACCESS_DENIED, details);
  }
}
