package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class MessageNotFoundException extends MessageException {

  public MessageNotFoundException() {
    super(ResultCode.MESSAGE_NOT_FOUND);
  }

  public MessageNotFoundException(Map<String, Object> details) {
    super(ResultCode.MESSAGE_NOT_FOUND, details);
  }
}
