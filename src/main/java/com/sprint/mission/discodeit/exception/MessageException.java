package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class MessageException extends DiscodeitException {

  public MessageException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  public static MessageException messageNotFound(Map<String, Object> details) {
    return new MessageException(ErrorCode.MESSAGE_NOT_FOUND, details);
  }


}
