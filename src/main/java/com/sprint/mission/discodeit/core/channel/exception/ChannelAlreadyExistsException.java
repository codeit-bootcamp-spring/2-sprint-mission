package com.sprint.mission.discodeit.core.channel.exception;

import com.sprint.mission.discodeit.exception.ChannelException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class ChannelAlreadyExistsException extends ChannelException {

  private final Object[] args;

  public ChannelAlreadyExistsException(ErrorCode errorCode, Object... args) {
    super(errorCode);
    this.args = args;
  }

  public Object[] getArgs() {
    return args;
  }
}
