package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.user.User;

import java.time.Instant;
import java.util.UUID;

public record UserUpdateResponseDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        UUID profileId
) {
    public static UserUpdateResponseDto fromEntity(User user) {
        return new UserUpdateResponseDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileId()
        );
    }
}