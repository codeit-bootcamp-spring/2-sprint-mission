package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.RestException;
import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class ReadStatusException extends RestException {

  public ReadStatusException(ResultCode resultCode) {
    super(resultCode);
  }

  public ReadStatusException(ResultCode resultCode,
      Map<String, Object> details) {
    super(resultCode, details);
  }
}
