package com.sprint.mission.discodeit.exceptions.binarycontent;

import com.sprint.mission.discodeit.exceptions.DiscodeitException;
import com.sprint.mission.discodeit.exceptions.ErrorCode;

import java.time.Instant;
import java.util.Map;

public class BinaryContentException extends DiscodeitException {
  public BinaryContentException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
    super(timestamp, errorCode, details);
  }
}
