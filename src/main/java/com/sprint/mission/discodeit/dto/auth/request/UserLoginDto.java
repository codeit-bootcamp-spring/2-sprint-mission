package com.sprint.mission.discodeit.dto.auth.request;

public record UserLoginDto(
        String userName,
        String userPassword
) {
}
