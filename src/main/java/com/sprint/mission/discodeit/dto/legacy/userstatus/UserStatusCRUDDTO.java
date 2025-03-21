package com.sprint.mission.discodeit.dto.legacy.userstatus;

import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserStatusCRUDDTO(
        UUID userStatusId,
        UUID userId,
        UserStatus userStatus
) {

    public static UserStatusCRUDDTO create(UUID userId,
                                           UserStatus userStatus) {
        return UserStatusCRUDDTO.builder()
                .userId(userId)
                .userStatus(userStatus).build();
    }

    public static UserStatusCRUDDTO update(UUID replaceId) {
        return UserStatusCRUDDTO.builder()
                .userStatusId(replaceId).build();
    }
}
