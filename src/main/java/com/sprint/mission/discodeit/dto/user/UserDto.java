package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;

import com.sprint.mission.discodeit.entity.Role;
import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String email,
        BinaryContentDto profile,
        Role role,
        Boolean online
) {
    public UserDto withOnline(Boolean isOnline) {
        return new UserDto(id, username, email, profile, role, isOnline);
    }
}
