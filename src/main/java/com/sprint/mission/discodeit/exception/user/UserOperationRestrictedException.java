package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserOperationRestrictedException extends UserException {
    public UserOperationRestrictedException() {
        super(ErrorCode.USER_OPERATION_RESTRICTED);
    }
} 