package com.sprint.mission.discodeit.controller.dto;

import com.sprint.mission.discodeit.entity.UserStatusType;

public record UserStatusUpdateResponse(
        boolean success,
        UserStatusType status
) {
}
