package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
public class UserStatus {
    private UUID userStatusId;
    private final UUID userId;
    public final Instant createdAt;
    public Instant updatedAt;
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
