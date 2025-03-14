package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String username,
        String email,
        UUID profileId,
        Instant createdAt,
        Instant updatedAt,
        boolean isActive
) {
    public UserResponseDto(User user, boolean isActive) {
        this(user.getId(), user.getUsername(), user.getEmail(), user.getProfileId(),
                user.getCreatedAt(), user.getUpdatedAt(), isActive);
    }
}
