package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class DuplicateEmailException extends UserException {

  public DuplicateEmailException() {
    super(ErrorCode.DUPLICATE_EMAIL);
  }

  public DuplicateEmailException(Map<String, Object> details) {
    super(ErrorCode.DUPLICATE_EMAIL, details);
  }
}
