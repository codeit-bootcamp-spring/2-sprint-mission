package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.RestException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserStatusException extends RestException {

  public UserStatusException(ErrorCode errorCode) {
    super(errorCode);
  }

  public UserStatusException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
