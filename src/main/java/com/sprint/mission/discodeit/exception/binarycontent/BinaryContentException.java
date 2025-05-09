package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.RestException;
import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class BinaryContentException extends RestException {

  public BinaryContentException(ResultCode resultCode) {
    super(resultCode);
  }

  public BinaryContentException(ResultCode resultCode, Map<String, Object> details) {
    super(resultCode, details);
  }
}
