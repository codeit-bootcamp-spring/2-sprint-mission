package com.sprint.mission.discodeit.dto.controller.user;

public record UpdateUserRequestDTO(
        String username,
        String email,
        String password
) {
}
