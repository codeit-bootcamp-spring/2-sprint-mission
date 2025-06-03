package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class BinaryContentNotFoundException extends BinaryContentException {

  public BinaryContentNotFoundException() {
    super(ErrorCode.BINARY_CONTENT_NOT_FOUND);
  }

  public static BinaryContentNotFoundException byId(UUID binaryContentId) {
    BinaryContentNotFoundException exception = new BinaryContentNotFoundException();
    exception.addDetail("binaryContentId", binaryContentId);
    return exception;
  }

}
