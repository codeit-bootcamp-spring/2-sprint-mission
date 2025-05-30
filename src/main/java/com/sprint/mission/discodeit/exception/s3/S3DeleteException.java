package com.sprint.mission.discodeit.exception.s3;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class S3DeleteException extends S3CustomException {

  public S3DeleteException() {
    super(ErrorCode.S3_DELETE_FAILED);
  }

  public S3DeleteException(Map<String, Object> details) {
    super(ErrorCode.S3_DELETE_FAILED, details);
  }
}
