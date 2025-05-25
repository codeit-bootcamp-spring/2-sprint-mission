package com.sprint.mission.discodeit.exception.s3;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.RestException;
import java.util.Map;

public class S3CustomException extends RestException {

  public S3CustomException(ErrorCode errorCode) {
    super(errorCode);
  }

  public S3CustomException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
