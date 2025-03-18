package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        boolean isOnline
) {}
