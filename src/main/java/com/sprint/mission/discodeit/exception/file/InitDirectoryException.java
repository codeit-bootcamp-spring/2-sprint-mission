package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class InitDirectoryException extends FileException {

  public InitDirectoryException() {
    super(ResultCode.INIT_DIRECTORY_ERROR);
  }

  public InitDirectoryException(Map<String, Object> details) {
    super(ResultCode.INIT_DIRECTORY_ERROR, details);
  }
}
