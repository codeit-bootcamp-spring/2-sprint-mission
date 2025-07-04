package com.sprint.mission.discodeit.exception.security;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class SessionExpiredException extends SecurityException {
  public SessionExpiredException() {
    super(ErrorCode.SESSION_EXPIRED);
  }

  public static SessionExpiredException sessionExpired() {
    return new SessionExpiredException();
  }
}
