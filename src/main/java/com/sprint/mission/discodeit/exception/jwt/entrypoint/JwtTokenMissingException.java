package com.sprint.mission.discodeit.exception.jwt.entrypoint;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class JwtTokenMissingException extends JwtAuthenticationException {

  public JwtTokenMissingException(String message) {
    super(message, ErrorCode.MISSING_JWT_TOKEN);
  }
}
