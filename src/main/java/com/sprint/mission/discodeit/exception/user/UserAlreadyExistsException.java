package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserAlreadyExistsException extends UserException {

    public UserAlreadyExistsException(String userNameOrEmail) {
        super(ErrorCode.INFO_DUPLICATE, Map.of("userNameOrEmail", userNameOrEmail));
    }
}
