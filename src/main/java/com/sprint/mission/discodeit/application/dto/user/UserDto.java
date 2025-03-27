package com.sprint.mission.discodeit.application.dto.user;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public record UserDto(UUID id, String name, String email, UUID profileId) {
    public static UserDto fromEntity(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getProfileId());
    }
}
