package com.sprint.mission.discodeit.DTO.User;

import com.sprint.mission.discodeit.entity.Status;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

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
) {
    public static UserDto fromUser(User user, UserStatus userStatus) {
        return new UserDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getProfileId(),
                user.getUsername(),
                user.getEmail(),
                userStatus.getStatus()
        );
    }
}

