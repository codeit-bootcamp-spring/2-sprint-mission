package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.common.BaseUpdatableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "user_statuses")
@Getter
@Setter
public class UserStatus extends BaseUpdatableEntity implements Persistable<UUID> {

    public static final long ONLINE_TIMEOUT_MINUTES = 5;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Instant lastActiveAt;

    @Transient
    private boolean isNew = true;

    protected UserStatus() {
    }

    public static UserStatus createUserStatus(User user, Instant lastActiveAt) {
        UserStatus userStatus = new UserStatus();
        userStatus.user = user;
        userStatus.lastActiveAt = lastActiveAt;
        user.setStatus(userStatus);

        return userStatus;
    }

    public boolean isOnline() {
        return Duration.between(lastActiveAt, Instant.now()).toMinutes() < ONLINE_TIMEOUT_MINUTES;
    }

    public void updateLastActiveAt(Instant newLastActiveAt) {
        this.lastActiveAt = newLastActiveAt;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PrePersist
    public void martNotNew() {
        this.isNew = false;
    }
}
