package com.sprint.mission.discodeit.exception.security;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class InvalidTokenException extends TokenException {

  public InvalidTokenException() {
    super(ErrorCode.INVALID_TOKEN);
  }

  public static InvalidTokenException invalidAccessToken(String accessToken) {
    InvalidTokenException exception = new InvalidTokenException();
    exception.addDetail("accessToken", accessToken);
    return exception;
  }

}
