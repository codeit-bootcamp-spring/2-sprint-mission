package com.sprint.mission.discodeit.service.dto.userdto;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserFindResponseDto(
        UUID userId,
        Instant createdAt,
        Instant updatedAt,
        String name,
        String email,
        UUID profileId,
        Boolean online
) {
    public static UserFindResponseDto UserFindResponse(User user, UserStatus userStatus) {
        return new UserFindResponseDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getName(),
                user.getEmail(),
                user.getProfileId(),
                userStatus.currentUserStatus()

        );
    }
}
