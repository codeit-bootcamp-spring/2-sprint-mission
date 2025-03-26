package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID userId;
    private Instant lastOnlineTime;

    public UserStatus(UUID userId, Instant lastOnlineTime) {
        this.userId = userId;
        this.lastOnlineTime = lastOnlineTime;
    }

    public boolean isOnline() {
        return lastOnlineTime.isAfter(Instant.now().minus(Duration.ofMinutes(5)));
    }

    public void updateUserStatus(Instant lastOnlineTime, Instant updateAt) {
        boolean anyValueUpdated = false;
        if (lastOnlineTime != null && !lastOnlineTime.equals(this.lastOnlineTime)) {
            this.lastOnlineTime = lastOnlineTime;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            updateUpdatedAt(updateAt);
        }
    }

    @Override
    public String toString() {
        return "UserStatus{" +
                "userId=" + userId +
                ", lastOnlineTime=" + lastOnlineTime +
                "} " + super.toString();
    }
}
