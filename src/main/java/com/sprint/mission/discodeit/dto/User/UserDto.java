package com.sprint.mission.discodeit.dto.User;

import com.sprint.mission.discodeit.entity.Status;

import java.time.Instant;
import java.util.UUID;

public record UserDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        UUID profileId,
        String username,
        String email,
        Status onlineStatus
) {}

