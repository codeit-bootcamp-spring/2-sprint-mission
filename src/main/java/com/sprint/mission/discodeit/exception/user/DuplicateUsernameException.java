package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class DuplicateUsernameException extends UserException {

  public DuplicateUsernameException() {
    super(ErrorCode.DUPLICATE_USERNAME);
  }

  public DuplicateUsernameException(Map<String, Object> details) {
    super(ErrorCode.DUPLICATE_USERNAME, details);
  }
}
