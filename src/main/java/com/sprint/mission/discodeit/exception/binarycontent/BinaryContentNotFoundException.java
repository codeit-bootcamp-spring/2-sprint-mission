package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class BinaryContentNotFoundException extends BinaryContentException {

  private static final ErrorCode DEFAULT_ERROR_CODE = ErrorCode.BINARY_CONTENTS_NOT_FOUND;

  public BinaryContentNotFoundException(Throwable cause, Map<String, Object> details) {
    super(cause, DEFAULT_ERROR_CODE, details);
  }

  public BinaryContentNotFoundException(Map<String, Object> details) {
    super(DEFAULT_ERROR_CODE, details);
  }

  public BinaryContentNotFoundException() {
    super(DEFAULT_ERROR_CODE);
  }

  public BinaryContentNotFoundException(Throwable cause) {
    super(DEFAULT_ERROR_CODE, cause);
  }
}
