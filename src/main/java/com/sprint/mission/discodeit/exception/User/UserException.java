package com.sprint.mission.discodeit.exception.User;

import com.sprint.mission.discodeit.exception.Basic.DiscodeitException;
import com.sprint.mission.discodeit.exception.Basic.ErrorCode;

import java.util.Map;

public class UserException extends DiscodeitException {
  public UserException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
