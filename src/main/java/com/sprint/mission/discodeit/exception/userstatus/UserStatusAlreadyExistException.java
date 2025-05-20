package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class UserStatusAlreadyExistException extends UserStatusException {

  public UserStatusAlreadyExistException(Map<String, Object> details) {
    super(Instant.now(), ErrorCode.DUPLICATE_USERSTATUS, details);
  }
}
