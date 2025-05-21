package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class DuplicateFilePathException extends FileException {

  public DuplicateFilePathException() {
    super(ResultCode.DUPLICATE_FILE_PATH);
  }

  public DuplicateFilePathException(Map<String, Object> details) {
    super(ResultCode.DUPLICATE_FILE_PATH, details);
  }
}
