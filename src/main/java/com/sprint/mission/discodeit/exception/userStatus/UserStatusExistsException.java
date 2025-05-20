package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class UserStatusExistsException extends UserStatusException {
    public UserStatusExistsException(UUID userId) {
        super(ErrorCode.USER_STATUS_ALREADY_EXISTS,
                Map.of("userId", userId)
        );
    }
}
