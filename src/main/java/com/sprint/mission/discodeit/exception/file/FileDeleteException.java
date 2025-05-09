package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class FileDeleteException extends FileException {

  public FileDeleteException() {
    super(ResultCode.FILE_DELETE_ERROR);
  }

  public FileDeleteException(Map<String, Object> details) {
    super(ResultCode.FILE_DELETE_ERROR, details);
  }
}
