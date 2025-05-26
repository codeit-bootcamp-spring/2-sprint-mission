package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ReadStatusException extends DiscodeitException {

  public ReadStatusException(Throwable cause,
      ErrorCode errorCode,
      Map<String, Object> details) {
    super(cause, errorCode, details);
  }

  public ReadStatusException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }

  public ReadStatusException(ErrorCode errorCode) {
    super(errorCode);
  }

  public ReadStatusException(ErrorCode errorCode,
      Throwable cause) {
    super(errorCode, cause);
  }
}
