package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ErrorDetailKey;
import java.util.Map;
import java.util.UUID;

public class ChannelNotFoundException extends ChannelException {

  public ChannelNotFoundException(UUID channelId) {
    super(ErrorCode.CHANNEL_NOT_FOUND, null, Map.of(ErrorDetailKey.CHANNEL_ID.getKey(), channelId));
  }
}
