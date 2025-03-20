package com.sprint.mission.discodeit.dto.user.request;

import java.util.Optional;
import java.util.UUID;

public record UserCreateRequest(
        String username,
        String email,
        String password,
        Optional<UUID> BinaryContentId
) {}
