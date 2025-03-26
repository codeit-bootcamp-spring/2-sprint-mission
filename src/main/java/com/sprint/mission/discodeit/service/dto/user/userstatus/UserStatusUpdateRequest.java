package com.sprint.mission.discodeit.service.dto.user.userstatus;

import com.sprint.mission.discodeit.controller.dto.user.UserStatusUpdateDataRequest;
import java.util.UUID;

public record UserStatusUpdateRequest(
        UUID userId,
        String status
) {
    public static UserStatusUpdateRequest of(UUID userId, UserStatusUpdateDataRequest data) {
        return new UserStatusUpdateRequest(userId, data.status());
    }
}
