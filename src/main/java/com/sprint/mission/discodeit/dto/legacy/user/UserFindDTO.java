package com.sprint.mission.discodeit.dto.legacy.user;

import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserFindDTO(
        UUID userid,
        UUID profileId,
        String userName,
        String email,
        Instant createdAt,
        Instant updatedAt,
        UserStatus userStatus
) {
    public static UserFindDTO find(
            UUID id,
            UUID profileId,
            String name,
            String email,
            Instant createdAt,
            Instant updatedAt,
            UserStatus userStatus
    ) {
        return UserFindDTO.builder()
                .userid(id)
                .profileId(profileId)
                .userName(name)
                .email(email)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .userStatus(userStatus).build();
    }

}

