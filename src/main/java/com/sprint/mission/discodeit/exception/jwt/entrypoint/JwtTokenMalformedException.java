package com.sprint.mission.discodeit.exception.jwt.entrypoint;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class JwtTokenMalformedException extends JwtAuthenticationException {

  public JwtTokenMalformedException(String message) {
    super(message, ErrorCode.MALFORMED_JWT_TOKEN);
  }
}
