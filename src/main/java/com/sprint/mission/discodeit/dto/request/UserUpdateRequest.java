package com.sprint.mission.discodeit.dto.request;

import java.util.Optional;
import java.util.UUID;

public record UserUpdateRequest(
        UUID id,
        Optional<String> username,
        Optional<String> email,
        Optional<String> password
) {}
