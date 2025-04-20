package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "user_statuses")
public class UserStatus extends BaseUpdatableEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    private User user;                // 주인 필드(외래키 소유)

    @Column(nullable = false)
    private Instant lastActiveAt = Instant.now();

    //1대1로 매핑
    public UserStatus(User user) {
        setUser(user);
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null && user.getUserStatus() != this) {
            user.setUserStatus(this);
        }
    }

    @Transient
    public boolean isOnline() {
        return lastActiveAt != null &&
            lastActiveAt.isAfter(Instant.now().minus(Duration.ofMinutes(5)));
    }
}

