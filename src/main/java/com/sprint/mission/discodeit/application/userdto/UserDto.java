package com.sprint.mission.discodeit.application.userdto;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public record UserDto(UUID id, String name, String email, UUID profileId, boolean isLogin) {
    public static UserDto fromEntity(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getProfileId(), user.getUserStatus().isLogin());
    }
}
