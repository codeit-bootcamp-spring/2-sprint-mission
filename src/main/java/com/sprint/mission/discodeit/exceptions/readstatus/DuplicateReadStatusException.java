package com.sprint.mission.discodeit.exceptions.readstatus;

import com.sprint.mission.discodeit.exceptions.ErrorCode;

import java.time.Instant;
import java.util.Map;

public class DuplicateReadStatusException extends ReadStatusException {
  public DuplicateReadStatusException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
    super(timestamp, errorCode, details);
  }

  public DuplicateReadStatusException(Map<String, Object> details) {
    super(Instant.now(), ErrorCode.DUPLICATE_READ_STATUS,  details);
  }
}
