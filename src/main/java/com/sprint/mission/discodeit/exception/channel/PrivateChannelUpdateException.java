package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class PrivateChannelUpdateException extends ChannelException {

  public PrivateChannelUpdateException() {
    super(ResultCode.FORBIDDEN_PRIVATE_CHANNEL);
  }

  public PrivateChannelUpdateException(Map<String, Object> details) {
    super(ResultCode.FORBIDDEN_PRIVATE_CHANNEL, details);
  }
}
