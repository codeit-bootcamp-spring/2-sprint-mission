package com.sprint.mission.discodeit.s3.exception;

import com.sprint.mission.discodeit.common.exception.DiscodeitException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;
import java.util.Map;

public abstract class S3CustomException extends DiscodeitException {

  public S3CustomException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

}
