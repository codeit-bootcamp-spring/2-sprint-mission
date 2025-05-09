package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class BinaryContentNotFoundException extends BinaryContentException {

  public BinaryContentNotFoundException() {
    super(ResultCode.BINARY_CONTENT_NOT_FOUND);
  }

  public BinaryContentNotFoundException(Map<String, Object> details) {
    super(ResultCode.BINARY_CONTENT_NOT_FOUND, details);
  }
}
