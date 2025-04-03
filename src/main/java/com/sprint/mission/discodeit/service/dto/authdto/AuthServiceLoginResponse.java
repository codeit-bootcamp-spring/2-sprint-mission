package com.sprint.mission.discodeit.service.dto.authdto;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public record AuthServiceLoginResponse(
        boolean success,
        UUID userId,
        UUID profileId

) {
    public static AuthServiceLoginResponse authLogin(boolean success, User user) {
        return new AuthServiceLoginResponse(success, user.getId(), user.getProfileId());
    }
}
