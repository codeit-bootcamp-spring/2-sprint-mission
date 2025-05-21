package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class DuplicateUsernameException extends UserException {

  public DuplicateUsernameException() {
    super(ResultCode.DUPLICATE_USERNAME);
  }

  public DuplicateUsernameException(Map<String, Object> details) {
    super(ResultCode.DUPLICATE_USERNAME, details);
  }
}
