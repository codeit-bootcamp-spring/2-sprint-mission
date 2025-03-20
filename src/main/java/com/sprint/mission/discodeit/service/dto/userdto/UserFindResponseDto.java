package com.sprint.mission.discodeit.service.dto.userdto;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserFindResponseDto(
        UUID userId,
        String name,
        String email,
        UUID profileId,
        Instant createdAt,
        String userStatus
) {
    public static UserFindResponseDto UserFindResponse(User user, UserStatus userStatus) {
        return new UserFindResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getProfileId(),
                user.getCreatedAt(),
                userStatus.currentUserStatus()

        );
    }
}
