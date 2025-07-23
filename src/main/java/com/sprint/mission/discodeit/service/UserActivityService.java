package com.sprint.mission.discodeit.service;

import java.time.Instant;
import java.util.UUID;

public interface UserActivityService {
    void updateLastActiveAt(UUID userId, Instant lastActiveAt);
}