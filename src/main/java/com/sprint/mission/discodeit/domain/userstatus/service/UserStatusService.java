package com.sprint.mission.discodeit.domain.userstatus.service;

import com.sprint.mission.discodeit.domain.userstatus.dto.UserStatusResult;

import java.time.Instant;
import java.util.UUID;

public interface UserStatusService {

    UserStatusResult updateByUserId(UUID userId, Instant instant);

}
