package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UpdateUserRequest(
        UUID userId,
        String username,
        String password,
        String email,
        String profileImageFileName,
        String profileImageFilePath
) {}
