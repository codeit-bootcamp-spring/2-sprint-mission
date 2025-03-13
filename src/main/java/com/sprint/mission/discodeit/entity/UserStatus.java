package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "user_status")
public class UserStatus extends BaseEntity {
    private UUID userId;
    private Instant lastSeenAt;

    public UserStatus(UUID id, UUID userId, Instant lastSeenAt) {
        super(id);
        this.userId = userId;
        this.lastSeenAt = lastSeenAt;
    }

    public boolean isOnline() {
        return lastSeenAt.isAfter(Instant.now().minusSeconds(300));
    }

    public void updateLastSeenAt() {
        this.lastSeenAt = Instant.now();
    }
}
