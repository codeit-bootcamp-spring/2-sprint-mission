package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserNameExistsException extends UserException {
    public UserNameExistsException(String username) {
        super(ErrorCode.USER_NAME_EXISTS, Map.of("username", username));
    }
}
