package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class EmailAlreadyExistsException extends UserException {

  public EmailAlreadyExistsException(String email) {
    super(ErrorCode.USER_ALREADY_EXISTS, Map.of("email", email));
  }
}
