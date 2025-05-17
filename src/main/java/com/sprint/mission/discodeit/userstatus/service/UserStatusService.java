package com.sprint.mission.discodeit.userstatus.service;

import com.sprint.mission.discodeit.userstatus.dto.UserStatusResult;

import java.time.Instant;
import java.util.UUID;

public interface UserStatusService {

    UserStatusResult updateByUserId(UUID userId, Instant instant);

}
