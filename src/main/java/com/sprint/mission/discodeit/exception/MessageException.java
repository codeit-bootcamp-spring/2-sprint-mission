package com.sprint.mission.discodeit.exception;

public class MessageException extends DiscodeitException {

  public MessageException(ErrorCode errorCode) {
    super(errorCode);
  }
}
