package com.sprint.mission.discodeit.dto.userstatus;

import com.sprint.mission.discodeit.model.UserStatusType;

public record UserStatusUpdateByUserIdRequest(
        UserStatusType type
) {
}
