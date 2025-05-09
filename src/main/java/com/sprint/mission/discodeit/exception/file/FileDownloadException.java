package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class FileDownloadException extends FileException {

  public FileDownloadException() {
    super(ResultCode.FILE_DOWNLOAD_ERROR);
  }

  public FileDownloadException(Map<String, Object> details) {
    super(ResultCode.FILE_DOWNLOAD_ERROR, details);
  }
}
