package com.sprint.mission.discodeit.exception.jwt;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.RestException;
import java.util.Map;

public class JwtException extends RestException {

  public JwtException(ErrorCode errorCode) {
    super(errorCode);
  }

  public JwtException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
