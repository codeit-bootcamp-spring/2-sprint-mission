package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class BinaryNotFoundException extends BinaryContentException {

  public BinaryNotFoundException(Map<String, Object> details) {
    super(Instant.now(), ErrorCode.BINARYCONTENT_NOT_FOUND, details);
  }
}
