package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.base.DiscodeitException;
import com.sprint.mission.discodeit.exception.base.ErrorCode;

public class UserStatusAlreadyExistsException extends DiscodeitException {

  public UserStatusAlreadyExistsException() {
    super(ErrorCode.USER_STATUS_ALREADY_EXISTS);
  }
}