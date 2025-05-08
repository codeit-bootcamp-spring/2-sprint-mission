package com.sprint.mission.discodeit.core.channel.exception;

import com.sprint.mission.discodeit.exception.ChannelException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class ChannelNotFoundException extends ChannelException {

  private final Object[] args;

  public ChannelNotFoundException(ErrorCode errorCode, Object... args) {
    super(errorCode);
    this.args = args;
  }

  public Object[] getArgs() {
    return args;
  }
}
