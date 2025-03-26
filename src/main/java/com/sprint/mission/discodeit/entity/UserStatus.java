package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus {
    private UUID id;
    private UUID userId;
    @Setter
    private String status;
    @Setter
    private Instant lastSeenAt;

    public UserStatus(UUID id, UUID userId, String status, Instant lastSeenAt) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.lastSeenAt = lastSeenAt;
    }

    public boolean isOnline() {
        return Instant.now().minusSeconds(300).isBefore(lastSeenAt);
    }

}