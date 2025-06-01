package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class FileDeleteException extends FileException {

  public FileDeleteException() {
    super(ErrorCode.FILE_DELETE_ERROR);
  }

  public FileDeleteException(Map<String, Object> details) {
    super(ErrorCode.FILE_DELETE_ERROR, details);
  }
}
