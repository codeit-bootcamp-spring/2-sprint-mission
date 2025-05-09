package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class FileReadException extends FileException {

  public FileReadException() {
    super(ResultCode.FILE_READ_ERROR);
  }

  public FileReadException(Map<String, Object> details) {
    super(ResultCode.FILE_READ_ERROR, details);
  }
}
