package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@ToString(onlyExplicitlyIncluded = true)
@Getter
public class UserStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ToString.Include
    private UUID userStatusId;
    @ToString.Include
    private final UUID userId;

    public final Instant createdAt;
    public Instant updatedAt;

    @ToString.Include
    public UserStatusType status;

    public UserStatus(UUID userId) {
        this(UUID.randomUUID(), userId, Instant.now());
    }

    public UserStatus(UUID userStatusId, UUID userId, Instant createdAt) {
        this.userStatusId = userStatusId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
        this.status = UserStatusType.ONLINE;
    }

    public void setUserStatusId(UUID userStatusId) {
        this.userStatusId = userStatusId;
        this.updatedAt = Instant.now();
    }

    public void updatedTime() {
        this.updatedAt = Instant.now();
    }

    public boolean isOnline() {
        return Duration.between(updatedAt, Instant.now()).getSeconds() <= 300;
    }

    public void updateStatus() {
        this.status = isOnline() ? UserStatusType.ONLINE : UserStatusType.OFFLINE;
    }



}
