package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class FileWriterException extends FileException {

  public FileWriterException() {
    super(ResultCode.FILE_WRITE_ERROR);
  }

  public FileWriterException(Map<String, Object> details) {
    super(ResultCode.FILE_WRITE_ERROR, details);
  }
}
