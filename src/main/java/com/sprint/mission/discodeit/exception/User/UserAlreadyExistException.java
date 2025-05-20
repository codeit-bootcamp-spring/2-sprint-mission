package com.sprint.mission.discodeit.exception.User;

import com.sprint.mission.discodeit.exception.Basic.ErrorCode;

import java.util.HashMap;
import java.util.Map;

public class UserAlreadyExistException extends UserException {
    public static UserAlreadyExistException duplicateUserName(String username) {
        Map<String, Object> details = new HashMap<>();
        details.put("username", username);
        return new UserAlreadyExistException(ErrorCode.DUPLICATE_USER, details);
    }

    public static UserAlreadyExistException duplicateEmail(String email) {
        Map<String, Object> details = new HashMap<>();
        details.put("email", email);
        return new UserAlreadyExistException(ErrorCode.DUPLICATE_USER, details);
    }

    public UserAlreadyExistException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}