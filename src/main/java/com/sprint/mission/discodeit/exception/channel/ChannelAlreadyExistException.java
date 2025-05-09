package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class ChannelAlreadyExistException extends ChannelException {

  public ChannelAlreadyExistException(Map<String, Object> details) {
    super(Instant.now(), ErrorCode.DUPLICATE_CHANNEL, details);
  }
}
