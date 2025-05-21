package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class DuplicateEmailException extends UserException {

  public DuplicateEmailException() {
    super(ResultCode.DUPLICATE_EMAIL);
  }

  public DuplicateEmailException(Map<String, Object> details) {
    super(ResultCode.DUPLICATE_EMAIL, details);
  }
}
