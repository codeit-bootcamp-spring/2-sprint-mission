package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class ReadStatusException extends DiscodeitException {

  public ReadStatusException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  public static ReadStatusException readStatusNotFound(Map<String, Object> details) {
    return new ReadStatusException(ErrorCode.READ_STATUS_NOT_FOUND, details);
  }

  public static ReadStatusException readStatusAlreadyExist(Map<String, Object> details) {
    return new ReadStatusException(ErrorCode.READ_STATUS_ALREADY_EXISTS, details);
  }
}
