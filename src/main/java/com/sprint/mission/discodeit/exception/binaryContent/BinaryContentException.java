package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public abstract class BinaryContentException extends DiscodeitException {

  public BinaryContentException(ErrorCode errorCode) {
    super(errorCode);
  }

  public BinaryContentException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public BinaryContentException(ErrorCode errorCode, String message,
      Map<String, Object> details) {
    super(errorCode, message, details);
  }
}
