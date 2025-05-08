package com.sprint.mission.discodeit.core.message.exception;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.MessageException;
import lombok.Getter;

@Getter
public class MessageInvalidRequestException extends MessageException {

  private final Object[] args;

  public MessageInvalidRequestException(ErrorCode errorCode, Object... args) {
    super(errorCode);
    this.args = args;
  }
}
