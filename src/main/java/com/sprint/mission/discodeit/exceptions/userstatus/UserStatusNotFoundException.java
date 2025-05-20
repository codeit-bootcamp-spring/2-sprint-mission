package com.sprint.mission.discodeit.exceptions.userstatus;

import com.sprint.mission.discodeit.exceptions.ErrorCode;

import java.time.Instant;
import java.util.Map;

public class UserStatusNotFoundException extends UserStatusException {
  public UserStatusNotFoundException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
    super(timestamp, errorCode, details);
  }
}
