package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserAlreadyExistsException extends UserException {

  public UserAlreadyExistsException(String email) {
    super(ErrorCode.DUPLICATE_USER, Map.of("email", email));
  }

  public UserAlreadyExistsException() {
    super(ErrorCode.DUPLICATE_USER, Map.of());
  }
}