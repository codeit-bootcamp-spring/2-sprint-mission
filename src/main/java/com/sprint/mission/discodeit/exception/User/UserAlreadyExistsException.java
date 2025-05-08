package com.sprint.mission.discodeit.exception.User;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserAlreadyExistsException extends UserException {

  public UserAlreadyExistsException(String email) {
    super(ErrorCode.EMAIL_ALREADY_EXISTS, Map.of("email", email));
  }
}
