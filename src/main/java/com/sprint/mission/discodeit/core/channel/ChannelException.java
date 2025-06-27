package com.sprint.mission.discodeit.core.channel;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class ChannelException extends DiscodeitException {

  public ChannelException(ErrorCode errorCode, Object... args) {
    super(errorCode, args);
  }
}
