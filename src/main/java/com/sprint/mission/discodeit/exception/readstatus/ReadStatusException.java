package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.RestException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ReadStatusException extends RestException {

  public ReadStatusException(ErrorCode errorCode) {
    super(errorCode);
  }

  public ReadStatusException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
