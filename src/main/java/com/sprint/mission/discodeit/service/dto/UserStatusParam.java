package com.sprint.mission.discodeit.service.dto;

import com.sprint.mission.discodeit.entity.UserStatusType;
import java.util.UUID;

public record UserStatusParam(
        UUID userId,
        UserStatusType status
) {
}
