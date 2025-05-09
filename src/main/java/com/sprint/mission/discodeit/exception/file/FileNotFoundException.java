package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class FileNotFoundException extends FileException {

  public FileNotFoundException() {
    super(ResultCode.FILE_NOT_FOUND);
  }

  public FileNotFoundException(Map<String, Object> details) {
    super(ResultCode.FILE_NOT_FOUND, details);
  }
}
