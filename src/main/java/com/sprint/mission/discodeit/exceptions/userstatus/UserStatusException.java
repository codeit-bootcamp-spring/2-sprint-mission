package com.sprint.mission.discodeit.exceptions.userstatus;

import com.sprint.mission.discodeit.exceptions.DiscodeitException;
import com.sprint.mission.discodeit.exceptions.ErrorCode;

import java.time.Instant;
import java.util.Map;

public class UserStatusException extends DiscodeitException {
  public UserStatusException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
    super(timestamp, errorCode, details);
  }
}
