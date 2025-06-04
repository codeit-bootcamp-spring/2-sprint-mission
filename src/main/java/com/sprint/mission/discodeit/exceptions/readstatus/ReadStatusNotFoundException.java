package com.sprint.mission.discodeit.exceptions.readstatus;

import com.sprint.mission.discodeit.exceptions.ErrorCode;

import java.time.Instant;
import java.util.Map;

public class ReadStatusNotFoundException extends ReadStatusException {
  public ReadStatusNotFoundException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
    super(timestamp, errorCode, details);
  }

  public ReadStatusNotFoundException(Map<String, Object> details) {
    super(Instant.now(), ErrorCode.READ_STATUS_NOT_FOUND, details);
  }
}
