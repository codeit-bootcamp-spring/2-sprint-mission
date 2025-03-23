package com.sprint.mission.discodeit.service.dto;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.entity.UserStatusType;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String email,
        String nickname,
        UserStatusType status,
        UserRole role,
        boolean isOnline
) {
    public static UserResponseDto convertToResponseDto(User user, boolean isOnline) {
        return new UserResponseDto(user.getId(), user.getEmail(), user.getNickname(), user.getStatus(), user.getRole(), isOnline);
    }

}
