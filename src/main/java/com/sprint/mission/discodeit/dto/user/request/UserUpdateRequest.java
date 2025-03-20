package com.sprint.mission.discodeit.dto.user.request;

import java.util.Optional;
import java.util.UUID;

public record UserUpdateRequest(
        UUID Id,
        Optional<String> username,
        Optional<String> email,
        Optional<String> password,
        Optional<UUID> BinaryContentId
) {}
