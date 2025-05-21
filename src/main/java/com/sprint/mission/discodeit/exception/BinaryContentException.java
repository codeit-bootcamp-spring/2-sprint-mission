package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.common.DiscodeitException;
import com.sprint.mission.discodeit.exception.common.ErrorCode;
import java.util.Map;

public class BinaryContentException extends DiscodeitException {

  public BinaryContentException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  public static BinaryContentException binaryContentNotFound(Map<String, Object> details) {
    return new BinaryContentException(ErrorCode.BINARY_CONTENT_NOT_FOUND, details);
  }
}
