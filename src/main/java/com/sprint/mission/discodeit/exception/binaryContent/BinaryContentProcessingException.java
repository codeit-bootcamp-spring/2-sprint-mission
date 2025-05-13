package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ErrorDetailKey;
import java.util.Map;

public class BinaryContentProcessingException extends BinaryContentException {

  public BinaryContentProcessingException(String filename) {
    super(ErrorCode.BINARY_CONTENT_PROCESSING_ERROR
        , null
        , Map.of(ErrorDetailKey.FILENAME.getKey(), filename));
  }
}
