package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Entity
@Table(name = "user_statuses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStatus extends BaseUpdatableEntity implements Serializable {

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    private Instant lastActiveAt;

    public UserStatus(User user, Instant lastActiveAt) {
        setUser(user);
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

    protected void setUser(User user) {
        this.user = user;
        user.setStatus(this);
    }
}
