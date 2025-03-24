package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private final UUID userId;
    private Instant lastActiveAt;

    public UserStatus(UUID userId) {
        validateReadStatus(userId);
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.userId = userId;
        this.lastActiveAt = Instant.now();
    }

    public void update(Instant lastActiveAt) {
        if(lastActiveAt != null && lastActiveAt.isAfter(this.lastActiveAt)){
            this.lastActiveAt = lastActiveAt;
            updateLastModifiedAt();
        }
    }

    public void updateByUserId() {
        this.lastActiveAt = Instant.now();
        updateLastModifiedAt();
    }

    private void updateLastModifiedAt() {
        this.updatedAt = Instant.now();
    }

    public boolean isOnline() {
        return lastActiveAt != null &&
                lastActiveAt.isAfter(Instant.now().minus(5, ChronoUnit.MINUTES));
    }

    @Override
    public String toString() {
        return "UserStatus{" +
                "id=" + id +
                ", lastActiveAt=" + lastActiveAt +
                ", userId=" + userId +
                '}';
    }

    /*******************************
     * Validation check
     *******************************/
    private void validateReadStatus(UUID userId){
        // 1. null check
        if (userId == null) {
            throw new IllegalArgumentException("userId가 없습니다.");
        }
    }

}
