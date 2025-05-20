package com.sprint.mission.discodeit.exceptions.channel;

import com.sprint.mission.discodeit.exceptions.ErrorCode;

import java.time.Instant;
import java.util.Map;

public class DuplicateChannelException extends ChannelException {
  public DuplicateChannelException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
    super(timestamp, errorCode, details);
  }
}
