package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class UserStatusException extends DiscodeitException {

  public UserStatusException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  public static UserStatusException userStatusNotFound(Map<String, Object> details) {
    return new UserStatusException(ErrorCode.USER_STATUS_NOT_FOUND, details);
  }

  public static UserStatusException userStatusAlreadyExist(Map<String, Object> details) {
    return new UserStatusException(ErrorCode.USER_STATUS_ALREADY_EXISTS, details);
  }
}
