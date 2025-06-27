package com.sprint.mission.discodeit.core.user.dto.request;

import com.sprint.mission.discodeit.core.user.entity.User;
import java.time.Instant;

public record UserStatusCreateRequest(
    User user,
    Instant lastActiveAt
) {

}
