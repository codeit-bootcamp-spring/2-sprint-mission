package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;

@Getter
public class UserStatus extends BaseUpdatableEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private User user;
    private Instant lastActiveAt;

    public UserStatus(User user, Instant lastActiveAt) {
        this.user = user;
        this.lastActiveAt = lastActiveAt;
    }

    public void updateLastActiveAt(Instant newLastActiveAt) {
        if (newLastActiveAt != null && !newLastActiveAt.equals(this.lastActiveAt)) {
            this.lastActiveAt = newLastActiveAt;
        }
    }

    public boolean isOnline() {
        return Instant.now().minusSeconds(300).isBefore(lastActiveAt);
    }
}
