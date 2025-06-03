package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class InvalidChannelUpdateException extends ChannelException {

  public InvalidChannelUpdateException() {
    super(ErrorCode.INVALID_CHANNEL_TYPE);
  }

  public static InvalidChannelUpdateException byId(UUID channelId) {
    InvalidChannelUpdateException exception = new InvalidChannelUpdateException();
    exception.addDetail("channelId", channelId);
    return exception;
  }
}
