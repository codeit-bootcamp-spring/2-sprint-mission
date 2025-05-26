package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class InvalidReadStatusRequestException extends ReadStatusException {

  private static final ErrorCode DEFAULT_ERROR_CODE = ErrorCode.INVALID_READ_STATUS_REQUEST;

  public InvalidReadStatusRequestException(Throwable cause, Map<String, Object> details) {
    super(cause, DEFAULT_ERROR_CODE, details);
  }

  public InvalidReadStatusRequestException(Map<String, Object> details) {
    super(DEFAULT_ERROR_CODE, details);
  }

  public InvalidReadStatusRequestException() {
    super(DEFAULT_ERROR_CODE);
  }

  public InvalidReadStatusRequestException(Throwable cause) {
    super(DEFAULT_ERROR_CODE, cause);
  }
}
