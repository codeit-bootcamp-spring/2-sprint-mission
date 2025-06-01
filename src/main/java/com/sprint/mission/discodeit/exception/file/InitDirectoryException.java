package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class InitDirectoryException extends FileException {

  public InitDirectoryException() {
    super(ErrorCode.INIT_DIRECTORY_ERROR);
  }

  public InitDirectoryException(Map<String, Object> details) {
    super(ErrorCode.INIT_DIRECTORY_ERROR, details);
  }
}
