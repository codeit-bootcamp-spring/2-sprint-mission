package com.sprint.mission.discodeit.exceptions.binarycontent;

import com.sprint.mission.discodeit.exceptions.ErrorCode;

import java.time.Instant;
import java.util.Map;

public class BinaryContentNotFoundException extends BinaryContentException {
  public BinaryContentNotFoundException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
    super(timestamp, errorCode, details);
  }
}
