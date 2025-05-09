package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.RestException;
import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class UserException extends RestException {

  public UserException(ResultCode resultCode) {
    super(resultCode);
  }

  public UserException(ResultCode resultCode, Map<String, Object> details) {
    super(resultCode, details);
  }
}
