package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class UserStatusAlreadyExsistsException extends UserStatusException {
  public UserStatusAlreadyExsistsException() {
    super(ErrorCode.DUPLICATE_USER_STATUS);
  }

  public UserStatusAlreadyExsistsException duplicateId(UUID id){
    this.putDetails("id", id);
    return this;
  }
}
