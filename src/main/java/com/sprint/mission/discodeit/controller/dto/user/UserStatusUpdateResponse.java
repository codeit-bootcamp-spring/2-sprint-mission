package com.sprint.mission.discodeit.controller.dto.user;

import com.sprint.mission.discodeit.entity.UserStatusType;

public record UserStatusUpdateResponse(
        boolean success,
        UserStatusType status
) {
}
