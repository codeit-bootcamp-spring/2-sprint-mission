package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class PrivateChannelUpdateException extends ChannelException {

  public PrivateChannelUpdateException() {
    super(ErrorCode.FORBIDDEN_PRIVATE_CHANNEL);
  }

  public PrivateChannelUpdateException(Map<String, Object> details) {
    super(ErrorCode.FORBIDDEN_PRIVATE_CHANNEL, details);
  }
}
