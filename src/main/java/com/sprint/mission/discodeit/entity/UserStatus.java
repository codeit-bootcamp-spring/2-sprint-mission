package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "user_statuses")
@Getter
public class UserStatus extends BaseUpdatableEntity {

    // 연관관계 주인 -> FK 를 가짐
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_user_status_user"))
    private User user;

    @Column(name = "last_active_at", nullable = false)
    private Instant lastActiveAt;

    protected UserStatus() {
    }

    public UserStatus(User user) {
        this.user = user;
        this.lastActiveAt = Instant.now();
    }

    public void updateLastActiveAt(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    public boolean online() {
        return lastActiveAt.isAfter(Instant.now().minus(5, ChronoUnit.MINUTES));
    }
}
