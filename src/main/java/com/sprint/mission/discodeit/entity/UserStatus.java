package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.Instant;


@Entity
@Table(name = "user_statuses")
@Getter
@NoArgsConstructor
public class UserStatus extends BaseUpdatableEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_status_user"))
    private User user;

    @Column(name = "last_active_at", nullable = false)
    private Instant lastActiveAt;

    @Builder
    public UserStatus(User user, Instant lastActiveAt) {
        super();
        this.user = user;
        this.lastActiveAt = lastActiveAt;
        if (user != null) {
            user.setStatus(this);
        }
    }

    public void updateLastActiveAt(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }
}
