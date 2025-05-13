package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ErrorDetailKey;
import java.util.Map;
import java.util.UUID;

public class ChannelUpdateNotAllowedException extends ChannelException {

  public ChannelUpdateNotAllowedException(UUID channelId, String message) {
    super(ErrorCode.CHANNEL_UPDATE_ALLOWED
        , message
        , Map.of(ErrorDetailKey.CHANNEL_ID.getKey(), channelId));
  }
}
