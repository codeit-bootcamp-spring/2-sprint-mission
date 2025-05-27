package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public abstract class BinaryContentException extends DiscodeitException {

  protected BinaryContentException(ErrorCode errorCode) {
    super(errorCode);
  }

  protected BinaryContentException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }

}
