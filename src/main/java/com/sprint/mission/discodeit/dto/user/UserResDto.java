package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.usertstatus.UserStatusResDto;

import java.util.UUID;

public record UserResDto(
        UUID id,
        String username,
        String email,
        UUID profileId,
        UserStatusResDto userStatusResDto
) {
}
