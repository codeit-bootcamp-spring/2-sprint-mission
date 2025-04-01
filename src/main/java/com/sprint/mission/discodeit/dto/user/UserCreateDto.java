package com.sprint.mission.discodeit.dto.user;

public record UserCreateDto(
        String username,
        String password,
        String email
) {
}
