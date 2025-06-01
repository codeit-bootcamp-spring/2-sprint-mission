package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.RestException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class MessageException extends RestException {

  public MessageException(ErrorCode errorCode) {
    super(errorCode);
  }

  public MessageException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
