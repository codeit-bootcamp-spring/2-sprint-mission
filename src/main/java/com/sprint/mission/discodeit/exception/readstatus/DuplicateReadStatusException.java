package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class DuplicateReadStatusException extends ReadStatusException {

  public DuplicateReadStatusException() {
    super(ResultCode.DUPLICATE_READ_STATUS);
  }

  public DuplicateReadStatusException(Map<String, Object> details) {
    super(ResultCode.DUPLICATE_READ_STATUS, details);
  }
}
