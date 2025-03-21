package com.sprint.mission.discodeit.service.dto.user;

public record UserCreateRequest(
        String username,
        String email,
        String password
) {
}
