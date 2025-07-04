package com.sprint.mission.discodeit.exception.jwt;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class InvalidRefreshTokenException extends JwtException {

  public InvalidRefreshTokenException() {
    super(ErrorCode.INVALID_REFRESH_TOKEN);
  }

  public InvalidRefreshTokenException(Map<String, Object> details) {
    super(ErrorCode.INVALID_REFRESH_TOKEN, details);
  }
}
