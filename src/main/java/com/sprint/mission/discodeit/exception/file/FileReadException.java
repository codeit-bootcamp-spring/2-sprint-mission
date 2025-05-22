package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class FileReadException extends FileException {

  public FileReadException() {
    super(ErrorCode.FILE_READ_ERROR);
  }

  public FileReadException(Map<String, Object> details) {
    super(ErrorCode.FILE_READ_ERROR, details);
  }
}
