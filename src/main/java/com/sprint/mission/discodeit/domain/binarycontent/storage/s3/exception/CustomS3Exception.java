package com.sprint.mission.discodeit.domain.binarycontent.storage.s3.exception;

import com.sprint.mission.discodeit.common.exception.DiscodeitException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;
import java.util.Map;

public abstract class CustomS3Exception extends DiscodeitException {

  public CustomS3Exception(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

}
