package com.sprint.mission.discodeit.DTO;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public record UserStatusCreateDTO(
        UUID userId,
        UserStatus userStatus
) {
}
