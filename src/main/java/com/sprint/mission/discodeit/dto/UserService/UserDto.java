package com.sprint.mission.discodeit.dto.UserService;

import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.UUID;

public record UserDto(
        UUID id,
        Instant createAt,
        Instant updateAt,
        String userName,
        String email,
        String password,
        UUID profileId
) {
}
