package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class MessageNotExistsInChannelException extends ChannelException {

  public MessageNotExistsInChannelException(UUID channelId) {
    super(ErrorCode.MESSAGE_NOT_FOUND, Map.of("channelId", channelId));
  }
}
