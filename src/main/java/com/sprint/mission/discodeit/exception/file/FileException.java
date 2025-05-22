package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.RestException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class FileException extends RestException {

  public FileException(ErrorCode errorCode) {
    super(errorCode);
  }

  public FileException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
