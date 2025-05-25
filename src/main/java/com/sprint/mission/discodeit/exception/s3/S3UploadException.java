package com.sprint.mission.discodeit.exception.s3;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class S3UploadException extends S3CustomException {

  public S3UploadException() {
    super(ErrorCode.S3_UPLOAD_FAILED);
  }

  public S3UploadException(Map<String, Object> details) {
    super(ErrorCode.S3_UPLOAD_FAILED, details);
  }
}
