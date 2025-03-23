package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    private static final long SERIAL_VERSION_UID = 1L;
    private final UUID id;
    private final UUID userId;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant lastLoginAt;
    private boolean isLogin;

    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.createdAt = ZonedDateTime.now().toInstant();
        this.updatedAt = createdAt;
        this.lastLoginAt = Instant.ofEpochSecond(0);
        this.isLogin = false;
    }

    public void updateLastLoginAt() {
        this.lastLoginAt = ZonedDateTime.now().toInstant();
        changeUserStatus();
    }


    public void changeUserStatus() {
        this.isLogin = !this.isLogin;
        updateLastModified();
    }

    private void updateLastModified() {
        this.updatedAt = ZonedDateTime.now().toInstant();
    }
}
