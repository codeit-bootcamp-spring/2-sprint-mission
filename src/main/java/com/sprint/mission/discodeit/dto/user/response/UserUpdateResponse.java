package com.sprint.mission.discodeit.dto.user.response;

import java.util.Optional;
import java.util.UUID;

public record UserUpdateResponse(
        UUID id,
        String username,
        String email,
        String password,
        Optional<UUID> BinaryContentId
) {}
