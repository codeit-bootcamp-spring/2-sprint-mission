package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.RestException;
import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class FileException extends RestException {

  public FileException(ResultCode resultCode) {
    super(resultCode);
  }

  public FileException(ResultCode resultCode, Map<String, Object> details) {
    super(resultCode, details);
  }
}
