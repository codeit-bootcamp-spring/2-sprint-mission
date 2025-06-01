package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.*;

public class UserNotFoundException extends UserException {

    public UserNotFoundException(String userId) {
        super(ErrorCode.USER_NOT_FOUND,
            Map.of("userId", userId));
    }
    
    public UserNotFoundException(Map<String, Object> additionalInfo) {
        super(ErrorCode.USER_NOT_FOUND, additionalInfo);
    }
}
