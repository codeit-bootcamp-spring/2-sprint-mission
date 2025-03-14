package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        UUID profileId,
        boolean isOnline
) {
    public static UserResponseDto fromEntity(User user, boolean isOnline) {
        return new UserResponseDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileId(),
                isOnline
        );
    }
}
