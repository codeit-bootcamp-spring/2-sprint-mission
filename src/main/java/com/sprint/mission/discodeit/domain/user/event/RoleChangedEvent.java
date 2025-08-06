package com.sprint.mission.discodeit.domain.user.event;

import com.sprint.mission.discodeit.domain.user.entity.Role;
import java.time.Instant;
import java.util.UUID;

public record RoleChangedEvent(
    Instant createdAt,
    UUID userId,
    Role previousRole,
    Role newRole
) {

  public RoleChangedEvent(UUID userId, Role previousRole, Role newRole) {
    this(Instant.now(), userId, previousRole, newRole);
  }
} 