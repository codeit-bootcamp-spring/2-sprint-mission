package com.sprint.mission.discodeit.service.userDto;

import java.util.UUID;

public record UserResponse (
        UUID id,
        String username,
        String email,
        boolean isOnline
){}

