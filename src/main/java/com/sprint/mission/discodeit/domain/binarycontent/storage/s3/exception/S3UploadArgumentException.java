package com.sprint.mission.discodeit.domain.binarycontent.storage.s3.exception;

import static com.sprint.mission.discodeit.common.exception.ErrorCode.ERROR_S3_UPLOAD_INVALID_ARGUMENT;

import java.util.Map;

public class S3UploadArgumentException extends CustomS3Exception {

  public S3UploadArgumentException(Map<String, Object> details) {
    super(ERROR_S3_UPLOAD_INVALID_ARGUMENT, details);
  }

}
