package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ReadStatusNotFoundException extends ReadStatusException {

  private final static ErrorCode DEFAULT_ERROR_CODE = ErrorCode.READ_STATUS_NOT_FOUND;

  public ReadStatusNotFoundException(Throwable cause, Map<String, Object> details) {
    super(cause, DEFAULT_ERROR_CODE, details);
  }

  public ReadStatusNotFoundException(Map<String, Object> details) {
    super(DEFAULT_ERROR_CODE, details);
  }

  public ReadStatusNotFoundException() {
    super(DEFAULT_ERROR_CODE);
  }

  public ReadStatusNotFoundException(Throwable cause) {
    super(DEFAULT_ERROR_CODE, cause);
  }
}
