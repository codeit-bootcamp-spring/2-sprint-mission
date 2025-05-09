package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class ReadStatusNotFoundException extends ReadStatusException {

  public ReadStatusNotFoundException(Map<String, Object> details) {
    super(Instant.now(), ErrorCode.READSTATUS_NOT_FOUND, details);
  }
}
