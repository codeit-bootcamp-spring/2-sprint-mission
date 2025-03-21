package com.sprint.mission.discodeit.dto.legacy.user;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserDTO(
        UUID userId,
        UUID profileId,
        String userName,
        String email,
        String password,
        Instant createdAt,
        Instant updatedAt,
        BinaryContent binaryContent,
        UserStatus userStatus
) {
}
