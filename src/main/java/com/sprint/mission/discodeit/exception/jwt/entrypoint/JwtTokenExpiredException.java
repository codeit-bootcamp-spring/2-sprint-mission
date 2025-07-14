package com.sprint.mission.discodeit.exception.jwt.entrypoint;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class JwtTokenExpiredException extends JwtAuthenticationException {

  public JwtTokenExpiredException(String message) {
    super(message, ErrorCode.EXPIRED_JWT_TOKEN);
  }
}
