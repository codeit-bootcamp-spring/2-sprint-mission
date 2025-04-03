package com.sprint.mission.discodeit.dto.UserStatus;

import java.time.Instant;

public record UpdateUserStatusDto(
        Instant newLastActiveAt
) {}
