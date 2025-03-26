package com.sprint.mission.discodeit.dto.auth;

import java.util.UUID;

public record AuthResDto(
        UUID id,
        String nickname,
        String email,
        UUID profileId
) {
}
