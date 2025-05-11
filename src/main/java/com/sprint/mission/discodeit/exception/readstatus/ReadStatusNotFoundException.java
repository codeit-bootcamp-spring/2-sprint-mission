package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class ReadStatusNotFoundException extends ReadStatusException {

  public ReadStatusNotFoundException() {
    super(ResultCode.READ_STATUS_NOT_FOUND);
  }

  public ReadStatusNotFoundException(Map<String, Object> details) {
    super(ResultCode.READ_STATUS_NOT_FOUND, details);
  }
}
