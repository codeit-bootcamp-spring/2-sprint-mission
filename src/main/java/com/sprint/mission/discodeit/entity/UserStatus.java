package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

@Getter
@Entity
@Table(name = "user_statuses")
public class UserStatus extends BaseUpdatableEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_DURATION = 5 * 60 * 1000;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "last_active_at")
    private Instant lastActiveAt;

    protected UserStatus() {
    }

    public UserStatus(User user, Instant lastActiveAt) {
        this.user = user;
        this.lastActiveAt = lastActiveAt;
    }

    public void updateLastActiveAt(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    public boolean isOnline(Instant now) {
        return Duration.between(this.lastActiveAt, now).toMillis() <= DEFAULT_DURATION;
    }
}
