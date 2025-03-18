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
    private Instant lastSeenAt;  // ✅ String → Instant로 변경

    public UserStatus(UUID id, UUID userId, String status, Instant lastSeenAt) {
        this.id = id;
        this.userId = userId;
        this.status = "ONLINE";
        this.lastSeenAt = lastSeenAt;
    }

    public boolean isOnline() {
        return "ONLINE".equalsIgnoreCase(this.status);
    }
}