package com.sprint.mission.discodeit.dto.auth.reslonse;

public record UserLoginResponse(
        boolean success,
        String message
) {
}
