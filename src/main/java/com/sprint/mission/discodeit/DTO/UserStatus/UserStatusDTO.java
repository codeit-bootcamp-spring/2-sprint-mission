package com.sprint.mission.discodeit.DTO.UserStatus;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.UserStatusType;

import java.time.Instant;
import java.util.UUID;

public record UserStatusDTO(
        UUID userStatusId,
        UUID userId,
        UserStatus userStatus,
        Instant createdAt,
        Instant updatedAt,
        UserStatusType status
) {
}
