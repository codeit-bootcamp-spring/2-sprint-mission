package com.sprint.mission.discodeit.service.dto.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.UserStatusType;
import java.time.Instant;
import java.util.UUID;


public record UserInfoResponse(
        UUID userId,
        Instant createAt,
        Instant updateAt,
        String username,
        String email,
        UUID profileId,
        UserStatusType status
) {
    public static UserInfoResponse of(User user, UserStatus userStatus) {
        return new UserInfoResponse(
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