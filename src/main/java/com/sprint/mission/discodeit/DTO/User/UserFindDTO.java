package com.sprint.mission.discodeit.DTO.User;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserFindDTO(
        UUID id,
        UUID profileId,

        String name,
        String email,
        Instant createdAt,
        Instant updatedAt,
        UserStatus userStatus
) {
}
