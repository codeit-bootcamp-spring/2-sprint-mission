package com.sprint.mission.discodeit.controller.dto;

import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.entity.UserStatusType;

public record UserUpdateResponseDto(
        String nickname,
        UserStatusType status,
        UserRole role
) {
}
