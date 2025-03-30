package com.sprint.mission.discodeit.DTO.UserStatus;

import com.sprint.mission.discodeit.entity.Status;

import java.time.Instant;
import java.util.UUID;

public record UpdateUserStatusDto(
        Instant newLastActiveAt
) {}
