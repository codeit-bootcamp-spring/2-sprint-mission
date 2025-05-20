package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_statuses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStatus extends BaseUpdatableEntity {
    private static final Duration MAX_ACTIVE_MINUTES = Duration.ofMinutes(5);

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_users_user_statuses"))
    private User user;

    @Column(nullable = false)
    private Instant lastActiveAt;

    public UserStatus(User user, Instant lastActiveAt) {
        setUser(user);
        this.lastActiveAt = lastActiveAt;
    }

    public void update(Instant lastActiveAt) {
        if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
            this.lastActiveAt = lastActiveAt;
        }
    }

    public boolean isOnline() {
        Instant now = Instant.now();
        Duration duration = Duration.between(this.lastActiveAt, now);

        return duration.compareTo(MAX_ACTIVE_MINUTES) < 0;
    }

    protected void setUser(User user) {
        this.user = user;
        user.setStatus(this);
    }
}
