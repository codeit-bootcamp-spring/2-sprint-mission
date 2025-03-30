package com.sprint.mission.discodeit.application.dto.user;

import java.util.UUID;

public record UserResponse(UUID id, String name, String email, UUID profileId, boolean isLogin) {
    public static UserResponse of(UserResult userResult, boolean isLogin) {
        return new UserResponse(userResult.id(), userResult.name(), userResult.email(), userResult.profileId(), isLogin);
    }
}
