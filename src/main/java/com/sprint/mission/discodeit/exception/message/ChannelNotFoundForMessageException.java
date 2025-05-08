package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class ChannelNotFoundForMessageException extends MessageException {

  public ChannelNotFoundForMessageException(UUID channelId) {
    super(ErrorCode.MESSAGE_NOT_FOUND, Map.of("channelId", channelId));
  }
}