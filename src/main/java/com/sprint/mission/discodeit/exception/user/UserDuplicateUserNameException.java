package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ErrorDetailKey;
import java.util.Map;

public class UserDuplicateUserNameException extends UserException {

  public UserDuplicateUserNameException(String userName) {
    super(ErrorCode.DUPLICATE_USER_USERNAME, null,
        Map.of(ErrorDetailKey.USER_NAME.getKey(), userName));
  }
}
