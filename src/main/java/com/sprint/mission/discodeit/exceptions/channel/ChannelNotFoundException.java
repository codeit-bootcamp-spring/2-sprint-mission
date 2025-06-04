package com.sprint.mission.discodeit.exceptions.channel;

import com.sprint.mission.discodeit.exceptions.ErrorCode;

import java.time.Instant;
import java.util.Map;

public class ChannelNotFoundException extends ChannelException {
  public ChannelNotFoundException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
    super(timestamp, errorCode, details);
  }

  public ChannelNotFoundException(Map<String, Object> details) {
    super(Instant.now(), ErrorCode.CHANNEL_NOT_FOUND, details);
  }
}
