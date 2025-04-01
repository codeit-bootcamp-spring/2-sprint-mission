package com.sprint.mission.discodeit.controller.dto;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.dto.UserResponseDto;

import java.time.Instant;
import java.util.UUID;

public record UserDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        UUID profileId,
        Boolean online
) {
    public static UserDto convertToDto(User user, boolean isOnline) {
        return new UserDto(user.getId(), user.getCreatedAt(), user.getUpdatedAt(), user.getNickname(), user.getEmail(), user.getProfileId(), isOnline);
    }
}
