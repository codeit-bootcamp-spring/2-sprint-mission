package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class WrongPasswordException extends DiscodeitException {
  public WrongPasswordException(Map<String, Object> details) {
    super(ErrorCode.WRONG_PASSWORD, details);
  }

  public WrongPasswordException() {
    super(ErrorCode.WRONG_PASSWORD, Map.of());
  }
}