package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class FileWriterException extends FileException {

  public FileWriterException() {
    super(ErrorCode.FILE_WRITE_ERROR);
  }

  public FileWriterException(Map<String, Object> details) {
    super(ErrorCode.FILE_WRITE_ERROR, details);
  }
}
