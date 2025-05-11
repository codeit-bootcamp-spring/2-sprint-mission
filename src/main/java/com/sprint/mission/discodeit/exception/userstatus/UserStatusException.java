package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.RestException;
import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class UserStatusException extends RestException {

  public UserStatusException(ResultCode resultCode) {
    super(resultCode);
  }

  public UserStatusException(ResultCode resultCode, Map<String, Object> details) {
    super(resultCode, details);
  }
}
