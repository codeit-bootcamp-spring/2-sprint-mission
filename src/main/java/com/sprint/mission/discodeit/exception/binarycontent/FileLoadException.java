package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.base.DiscodeitException;
import com.sprint.mission.discodeit.exception.base.ErrorCode;

import java.util.Map;

public class FileLoadException extends DiscodeitException {

  public FileLoadException() {
    super(ErrorCode.FILE_LOAD_ERROR);
  }

  public FileLoadException(Map<String, Object> details) {
    super(ErrorCode.FILE_LOAD_ERROR, details);
  }
}