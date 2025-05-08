package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.common.DiscodeitException;
import com.sprint.mission.discodeit.exception.common.ErrorCode;
import java.util.Map;

public class UserException extends DiscodeitException {

  public UserException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  public static UserException userNotFound(Map<String, Object> details) {
    return new UserException(ErrorCode.USER_NOT_FOUND, details);
  }

  public static UserException usernameNotFound(Map<String, Object> details) {
    return new UserException(ErrorCode.USERNAME_NOT_FOUND, details);
  }

  public static UserException usernameAlreadyExist(Map<String, Object> details) {
    return new UserException(ErrorCode.USERNAME_ALREADY_EXISTS, details);
  }

  public static UserException emailAlreadyExist(Map<String, Object> details) {
    return new UserException(ErrorCode.EMAIL_ALREADY_EXISTS, details);
  }
}