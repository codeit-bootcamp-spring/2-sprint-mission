package com.sprint.mission.discodeit.service.dto.userdto;

import java.util.UUID;

public record UserFindDto(
        UUID userId,
        String name,
        String email,
        String createdAt,
        boolean updateUserStatus
) {
}
