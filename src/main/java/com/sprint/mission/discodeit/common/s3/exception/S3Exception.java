package com.sprint.mission.discodeit.common.s3.exception;

import com.sprint.mission.discodeit.common.exception.DiscodeitException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;
import java.util.Map;

public abstract class S3Exception extends DiscodeitException {

  public S3Exception(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

}
