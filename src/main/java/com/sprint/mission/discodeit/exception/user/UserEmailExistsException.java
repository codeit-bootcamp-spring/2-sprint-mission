package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserEmailExistsException extends UserException {
    public UserEmailExistsException(String email) {
        super(ErrorCode.USER_EMAIL_EXISTS, Map.of("email", email));
    }
}
