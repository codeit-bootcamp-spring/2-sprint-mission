package com.sprint.mission.discodeit.exception.jwt.entrypoint;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class JwtSignatureException extends JwtAuthenticationException {

  public JwtSignatureException(String message) {
    super(message, ErrorCode.INVALID_JWT_SIGNATURE);
  }
}
