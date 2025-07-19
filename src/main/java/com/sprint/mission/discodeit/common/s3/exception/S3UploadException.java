package com.sprint.mission.discodeit.common.s3.exception;

import static com.sprint.mission.discodeit.common.exception.ErrorCode.ERROR_S3_UPLOAD;

import java.util.Map;

public class S3UploadException extends S3Exception {

  public S3UploadException(Map<String, Object> details) {
    super(ERROR_S3_UPLOAD, details);
  }

  public S3UploadException() {
    super(ERROR_S3_UPLOAD, Map.of());
  }

}
