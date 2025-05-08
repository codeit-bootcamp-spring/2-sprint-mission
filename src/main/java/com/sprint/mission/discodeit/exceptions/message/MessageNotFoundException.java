package com.sprint.mission.discodeit.exceptions.message;

import com.sprint.mission.discodeit.exceptions.DiscodeitException;
import com.sprint.mission.discodeit.exceptions.ErrorCode;

import java.time.Instant;
import java.util.Map;

public class MessageNotFoundException extends MessageException {
  public MessageNotFoundException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
    super(timestamp, errorCode, details);
  }
}
