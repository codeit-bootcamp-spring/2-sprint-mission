package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class DuplicateFilePathException extends FileException {

  public DuplicateFilePathException() {
    super(ErrorCode.DUPLICATE_FILE_PATH);
  }

  public DuplicateFilePathException(Map<String, Object> details) {
    super(ErrorCode.DUPLICATE_FILE_PATH, details);
  }
}
