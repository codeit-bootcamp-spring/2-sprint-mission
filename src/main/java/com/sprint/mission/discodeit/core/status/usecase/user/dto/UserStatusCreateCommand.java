package com.sprint.mission.discodeit.core.status.usecase.user.dto;

import com.sprint.mission.discodeit.core.user.entity.User;
import java.time.Instant;

public record UserStatusCreateCommand(
//    UUID userId,
    User user,
    Instant lastActiveAt
) {

}
