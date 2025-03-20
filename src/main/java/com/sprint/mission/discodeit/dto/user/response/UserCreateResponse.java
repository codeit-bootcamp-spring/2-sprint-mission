package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.domain.UserStatus;

import java.util.UUID;

public record UserCreateResponse(
        UUID id,
        String username,
        String email,
        String password
) {}
