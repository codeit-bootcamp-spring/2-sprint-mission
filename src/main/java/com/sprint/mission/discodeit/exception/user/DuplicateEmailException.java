package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.base.DiscodeitException;
import com.sprint.mission.discodeit.exception.base.ErrorCode;
import java.util.Map;

public class DuplicateEmailException extends DiscodeitException {

  public DuplicateEmailException() {
    super(ErrorCode.EMAIL_ALREADY_EXISTS);
  }

  public DuplicateEmailException(Map<String, Object> details) {
    super(ErrorCode.EMAIL_ALREADY_EXISTS, details);
  }
}
