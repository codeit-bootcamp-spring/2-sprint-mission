package com.sprint.mission.discodeit.exception.security;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class TokenNotFoundException extends TokenException {

  public TokenNotFoundException() {
    super(ErrorCode.TOKEN_NOT_FOUND);
  }

  public static TokenNotFoundException refreshTokenNotFound(String refreshToken) {
    TokenNotFoundException exception = new TokenNotFoundException();
    exception.addDetail("refreshToken", refreshToken);
    return exception;
  }

}
