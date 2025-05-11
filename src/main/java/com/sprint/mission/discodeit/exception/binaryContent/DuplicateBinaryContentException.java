package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;
import jdk.incubator.vector.VectorOperators.Binary;

public class DuplicateBinaryContentException extends BinaryContentException {
  public DuplicateBinaryContentException(
      Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
    super(timestamp, errorCode, details);
  }

}
