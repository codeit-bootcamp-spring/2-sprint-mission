package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.RestException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class BinaryContentException extends RestException {

  public BinaryContentException(ErrorCode errorCode) {
    super(errorCode);
  }

  public BinaryContentException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
