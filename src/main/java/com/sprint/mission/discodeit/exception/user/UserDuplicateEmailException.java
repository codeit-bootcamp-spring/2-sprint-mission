package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ErrorDetailKey;
import java.util.Map;

public class UserDuplicateEmailException extends UserException {

  public UserDuplicateEmailException(String email) {
    super(ErrorCode.DUPLICATE_USER_EMAIL, null, Map.of(ErrorDetailKey.EMAIL.getKey(), email));
  }
}
