package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Role;
import lombok.Getter;

import java.util.UUID;

@Getter
public class RoleChangedEvent {
    private final UUID userId;
    private final Role oldRole;
    private final Role newRole;

    public RoleChangedEvent(UUID userId, Role oldRole, Role newRole) {
        this.userId = userId;
        this.oldRole = oldRole;
        this.newRole = newRole;
    }
}
