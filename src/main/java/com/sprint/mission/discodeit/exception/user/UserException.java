package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public abstract class UserException extends DiscodeitException {

  public UserException(ErrorCode errorCode) {
    super(errorCode);
  }

  public UserException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public UserException(ErrorCode errorCode, String message, Map<String, Object> details) {
    super(errorCode, message, details);
  }
}
