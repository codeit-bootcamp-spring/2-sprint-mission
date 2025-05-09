package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class UserStatusNotFoundException extends UserStatusException {
    public UserStatusNotFoundException(String key, UUID value) {
        super(ErrorCode.USER_STATUS_NOT_FOUND, Map.of(key, value));
    }
    public static UserStatusNotFoundException forUserId(UUID userId) {
        return new UserStatusNotFoundException("userId", userId);
    }
    public static UserStatusNotFoundException forUserStatusId(UUID userstatusId) {
        return new UserStatusNotFoundException("userstatusId", userstatusId);
    }
}