package com.sprint.mission.discodeit.service.dto.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;


public record UserDto(
        UUID id,
        Instant createAt,
        Instant updateAt,
        String username,
        String email,
        UUID profileId,
        Boolean online
) {
    public static UserDto of(User user, UserStatus userStatus) {
        return new UserDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileId(),
                userStatus.isOnline()
        );
    }
}