package com.sprint.mission.discodeit.core.channel.exception;

import com.sprint.mission.discodeit.exception.ChannelException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class ChannelLoginFailedException extends ChannelException {

  private final Object[] args;

  public ChannelLoginFailedException(ErrorCode errorCode, Object... args) {
    super(errorCode);
    this.args = args != null ? args : new Object[0];
  }

  public Object[] getArgs() {
    return args;
  }
}
