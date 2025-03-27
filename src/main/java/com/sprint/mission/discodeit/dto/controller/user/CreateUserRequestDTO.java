package com.sprint.mission.discodeit.dto.controller.user;

import java.util.UUID;

public record CreateUserRequestDTO(
    String username,
    String password,
    String email
) {
}
