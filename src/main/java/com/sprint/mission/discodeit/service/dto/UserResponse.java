package com.sprint.mission.discodeit.service.dto;

import java.util.UUID;

public record UserResponse (
        UUID id,
        String username,
        String email,
        boolean isOnline
){}

