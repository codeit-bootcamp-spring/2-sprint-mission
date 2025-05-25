package com.sprint.mission.discodeit.exception.s3;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class S3DownloadException extends S3CustomException {

  public S3DownloadException() {
    super(ErrorCode.S3_DOWNLOAD_FAILED);
  }

  public S3DownloadException(Map<String, Object> details) {
    super(ErrorCode.S3_DOWNLOAD_FAILED, details);
  }
}
