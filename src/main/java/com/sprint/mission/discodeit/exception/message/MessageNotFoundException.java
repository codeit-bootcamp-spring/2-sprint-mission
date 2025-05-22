package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class MessageNotFoundException extends MessageException {

  private static final ErrorCode DEFAULT_ERROR_CODE = ErrorCode.MESSAGE_NOT_FOUND;

  public MessageNotFoundException(Throwable cause, Map<String, Object> details) {
    super(cause, DEFAULT_ERROR_CODE, details);
  }

  public MessageNotFoundException(Map<String, Object> details) {
    super(DEFAULT_ERROR_CODE, details);
  }

  public MessageNotFoundException() {
    super(DEFAULT_ERROR_CODE);
  }

  public MessageNotFoundException(Throwable cause) {
    super(DEFAULT_ERROR_CODE, cause);
  }
}
