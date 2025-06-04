package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class BinaryContentException extends DiscodeitException {

  public BinaryContentException(Throwable cause, ErrorCode errorCode,
      Map<String, Object> details) {
    super(cause, errorCode, details);
  }

  public BinaryContentException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  public BinaryContentException(ErrorCode errorCode) {
    super(errorCode);
  }

  public BinaryContentException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }
}
