package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "user_statuses")
public class UserStatus extends BaseUpdatableEntity {

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Instant lastActiveAt;


    public UserStatus(User user, Instant lastActiveAt) {
        this.user = user;
        this.lastActiveAt = lastActiveAt;
    }

    // 마지막 접속시간과 현재시간을 계산했을 때 300보다 작으면 : 5분 이내 -> true
    // 마지막 접속시간과 현재시간을 계산했을 때 300보다 크면 : 5분 초과 -> false
    public boolean currentUserStatus() {
        if (Duration.between(lastActiveAt, Instant.now()).getSeconds() < 300) {
            return true;
        } else {
            return false;
        }
    }

    public void updateLastConnectionTime(Instant LastConnectionTime) {
        this.lastActiveAt = LastConnectionTime;
        currentUserStatus();

    }

    @Override
    public String toString() {
        return "\nID: " + getId() +
                "\nUser ID: " + user +
                "\nLastConnectionTime: " + getLastActiveAt() +
                "\nUser Status: " + currentUserStatus();

    }
}
