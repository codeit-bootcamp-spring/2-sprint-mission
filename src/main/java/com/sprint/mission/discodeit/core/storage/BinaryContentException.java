package com.sprint.mission.discodeit.core.storage;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class BinaryContentException extends DiscodeitException {

  public BinaryContentException(ErrorCode errorCode, Object... args) {
    super(errorCode,args);
  }
}
