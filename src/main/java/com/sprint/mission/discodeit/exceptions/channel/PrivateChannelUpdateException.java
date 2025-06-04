package com.sprint.mission.discodeit.exceptions.channel;

import com.sprint.mission.discodeit.exceptions.ErrorCode;

import java.time.Instant;
import java.util.Map;

public class PrivateChannelUpdateException extends ChannelException {
  public PrivateChannelUpdateException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
    super(timestamp, errorCode, details);
  }

  public PrivateChannelUpdateException(Map<String, Object> details) {
    super(Instant.now(), ErrorCode.PRIVATE_CHANNEL_UPDATE, details);
  }
}
