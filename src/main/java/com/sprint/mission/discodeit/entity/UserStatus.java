package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
import java.io.Serializable;

@Getter
public class UserStatus implements Serializable{
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final UUID userId;
    private final Instant craetedAt;
    private Instant updatedAt;
    private Instant lastOnlineAt;
    private boolean online;

    public UserStatus(UUID userId, Instant lastOnlineAt) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.craetedAt = Instant.now();
        this.updatedAt = Instant.now();
        this.lastOnlineAt = lastOnlineAt;
        this.online = isOnline();
    }

    public Instant updateLastOnline(Instant lastOnlineAt) {
        this.lastOnlineAt = lastOnlineAt;
        this.online = isOnline();
        return lastOnlineAt;
    }

    public boolean isOnline(){
        Instant fiveMinuteAgo = Instant.now().minusSeconds(5 * 60);

        return lastOnlineAt.isAfter(fiveMinuteAgo);
    }

}
