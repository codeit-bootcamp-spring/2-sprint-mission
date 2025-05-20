package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.constant.ErrorCode;

public class UserWrongPasswordException extends UserException {

    public UserWrongPasswordException() {
        super(ErrorCode.WRONG_PASSWORD);
    }
}
