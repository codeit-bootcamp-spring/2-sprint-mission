package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class UserNotFoundException extends UserException {
  public UserNotFoundException() {
    super(ErrorCode.USER_NOT_FOUND);
  }

  public UserNotFoundException notFoundWithId(UUID id){
    this.putDetails("id", id);
    return this;
  }

  public UserNotFoundException notFoundWithUsername(String username){
    this.putDetails("username", username);
    return this;
  }
}
