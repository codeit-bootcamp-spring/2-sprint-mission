package com.sprint.mission.discodeit.application.dto.user;

import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.UUID;

public record UserResult(UUID id, Instant createdAt, Instant updatedAt, String username, String email, UUID profileId,
                         boolean online) {

    public static UserResult fromEntity(User user, boolean isOnline) {
        return new UserResult(user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getName(),
                user.getEmail(),
                user.getBinaryContent().getId(),
                isOnline);
    }
}
