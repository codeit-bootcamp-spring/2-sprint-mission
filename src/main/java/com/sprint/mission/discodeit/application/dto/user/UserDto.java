package com.sprint.mission.discodeit.application.dto.user;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public record UserDto(UUID id, String name, String email, UUID profileId, boolean isLogin) {
    public static UserDto fromEntity(User user) { // TODO: 3/23/25  isLogin 부분 수정필요
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getProfileId(), true);
    }
}
