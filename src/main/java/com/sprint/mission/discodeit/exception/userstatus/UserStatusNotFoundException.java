package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class UserStatusNotFoundException extends UserStatusException {

  public UserStatusNotFoundException(Map<String, Object> details) {
    super(Instant.now(), ErrorCode.USERSTATUS_NOT_FOUND, details);
  }
}
