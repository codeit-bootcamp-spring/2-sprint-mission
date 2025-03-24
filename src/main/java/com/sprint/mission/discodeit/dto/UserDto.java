package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UserDto(
        UUID userId,
        String userName,
        String userEmail,
        String userPassword,
        UUID profileId,
        boolean isOnline
) {}