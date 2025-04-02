package com.sprint.mission.discodeit.dto.service.user;

import java.util.UUID;

public record CreateUserParam(
        String username,
        String email,
        String password
) {
}
