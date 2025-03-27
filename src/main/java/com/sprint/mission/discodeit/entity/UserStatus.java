package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    //
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    //
    private UserStatusType type;
    private Instant activatedAt;
    private UUID userId;

    public UserStatus(UUID userId, Instant activatedAt) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.type = UserStatusType.OFFLINE;
        this.userId = userId;
        this.activatedAt = activatedAt;
    }

    public void update(Instant newActivatedAt) {
        boolean anyValueUpdated = false;
        if(activatedAt != null&&!newActivatedAt.equals(activatedAt)) {
            this.activatedAt = newActivatedAt;
            anyValueUpdated = true;
        }
        if(anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }

    public boolean isOnline() {
        Duration duration = Duration.between(activatedAt, Instant.now());
        if (duration.toMinutes() <= 5) {
            if (type != UserStatusType.ONLINE) {
                type = UserStatusType.ONLINE;
            }
            return true;
        }
        if (type != UserStatusType.OFFLINE) {
            type = UserStatusType.OFFLINE;
        }
        return false;
    }
}
