package com.sprint.mission.discodeit.exception.jwt.entrypoint;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class JwtUnsupportedException extends JwtAuthenticationException {

  public JwtUnsupportedException(String message) {
    super(message, ErrorCode.UNSUPPORTED_JWT_TOKEN);
  }
}
