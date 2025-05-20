package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class InvalidChannelUpdateException extends ChannelException {

  public InvalidChannelUpdateException(UUID channelId) {
    super(ErrorCode.INVALID_CHANNEL_TYPE, Map.of("channelId", channelId));
  }
}
