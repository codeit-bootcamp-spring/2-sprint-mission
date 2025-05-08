package com.sprint.mission.discodeit.core.channel.exception;

import com.sprint.mission.discodeit.exception.ChannelException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ChannelInvalidRequestException extends ChannelException {

  private final Object[] args;

  public ChannelInvalidRequestException(ErrorCode errorCode, Object... args) {
    super(errorCode);
    this.args = args;
  }
}
