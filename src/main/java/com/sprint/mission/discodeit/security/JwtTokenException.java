package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class JwtTokenException extends DiscodeitException {

  public JwtTokenException(ErrorCode errorCode, Object... args) {
    super(errorCode, args);
  }
}
