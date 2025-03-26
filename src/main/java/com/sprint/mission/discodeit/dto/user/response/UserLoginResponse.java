package com.sprint.mission.discodeit.dto.user.response;

public record UserLoginResponse(
        String username,
        String password,
        String email
) {
}
