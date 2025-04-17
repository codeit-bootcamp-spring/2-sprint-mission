package com.sprint.mission.discodeit.service.dto.request.userstatusdto;

import java.util.UUID;

public record UserStatusFindDto(
        UUID userId,
        String name,
        String email,
        String createdAt,
        boolean updateUserStatus
) {
}
