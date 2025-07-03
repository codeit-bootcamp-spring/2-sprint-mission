package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.*;

public class UserNotFoundException extends UserException {

    public UserNotFoundException(Object userId) {
        super(ErrorCode.USER_NOT_FOUND, (String) userId);
    }
}
