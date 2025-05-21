package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.RestException;
import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class MessageException extends RestException {

  public MessageException(ResultCode resultCode) {
    super(resultCode);
  }

  public MessageException(ResultCode resultCode,
      Map<String, Object> details) {
    super(resultCode, details);
  }
}
