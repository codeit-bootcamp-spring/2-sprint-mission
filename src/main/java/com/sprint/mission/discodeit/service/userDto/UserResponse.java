package com.sprint.mission.discodeit.service.userDto;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserResponse (
        UUID id,
        String username,
        String email,
        boolean isOnline
){

}

