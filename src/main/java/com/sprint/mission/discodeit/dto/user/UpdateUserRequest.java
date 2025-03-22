package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UpdateUserRequest(
        UUID userId,
        String newName,
        String newEmail,
        String profileImageFileName,
        String profileImageFilePath
) {}
