package com.sprint.mission.discodeit.controller.dto;

import com.sprint.mission.discodeit.entity.UserStatus;

public record UserStatusUpdateResponse(
        boolean success,
        String status
) {
    public static UserStatusUpdateResponse of(UserStatus status) {
        if (status.isOnline()) {
            return new UserStatusUpdateResponse(true, "online");
        }
        return new UserStatusUpdateResponse(true, "offline");
    }
}
